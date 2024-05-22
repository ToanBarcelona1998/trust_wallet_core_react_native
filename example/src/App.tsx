import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  createMnemonic,
  validateMnemonic,
  Coin,
  CoinType,
  CoinParameter,
} from 'trust_wallet_core_react_native';

import { ethers } from 'ethers';

function bigintToHexString(value: bigint): string {
  // Convert bigint to a hexadecimal string without the "0x" prefix
  let hexString = value.toString(16);

  // Ensure the hex string has an even number of characters by padding with a leading zero if necessary
  if (hexString.length % 2 !== 0) {
    hexString = '0' + hexString;
  }

  // Add the "0x" prefix
  return '0x' + hexString;
}

// function toHexString(byteArray: Uint8Array) {
//   return Array.from(byteArray, function (byte) {
//     return ('0' + (byte & 0xff).toString(16)).slice(-2);
//   }).join('');
// }

export default function App() {
  const [result, setResult] = React.useState<String>('');

  React.useEffect(() => {
    createMnemonic(128, '').then(setResult);
  }, []);

  function validate() {
    validateMnemonic(result, '').then((isValid) => {
      setResult(`${isValid}`);
      console.log(isValid);
    });
  }

  async function signTransaction() {
    try {
      console.log('run 0');

      var provider = new ethers.JsonRpcProvider(
        'https://ethereum-sepolia-rpc.publicnode.com'
      );

      console.log('run1');

      let address = await Coin.getAddress(
        CoinType.Ethereum,
        'inch device rain tired suffer voyage release stick ostrich vacant surface equal',
        ''
      );

      console.log(`run 2 ${address}`);

      let nonce = await provider.getTransactionCount(`${address}`);

      console.log(`run 3`);

      let chainId = (await provider.getNetwork()).chainId;

      console.log(`run 4`);
      //insufficient funds for intrinsic transaction cost (transaction="0xf86f0b850d693a400082520894ec552cfb5ad7d7f8fb6aa5d832487fcf1c2f04eb870348bca5a16000808401546d72a00a1eb11a7fd7a50917a6077f895bd7a20cd0e67b98f1af33b895ce6917baf7e4a071826f0fb29e5bfd1b281d7d0ca67fa14766e685d8bdcdde248642a209e017f7", info={ "error": { "code": -32000, "message": "insufficient funds for gas * price + value: balance 0, tx cost 2134000000000000, overshot 2134000000000000" } }, code=INSUFFICIENT_FUNDS, version=6.12.1)

      let ethereumTransaction =
        new CoinParameter.Ethereum.TransferEthereumTransaction(
          '0x0348bca5a16000',
          '0xeC552cFb5Ad7d7f8FB6aA5D832487Fcf1C2f04EB'
        );

      let ethereumParameter =
        new CoinParameter.Ethereum.EthereumTransactionParameter(
          bigintToHexString(chainId),
          bigintToHexString(BigInt(nonce)),
          '0xd693a4000',
          '0x5208',
          ethereumTransaction
        );

      let tx = await Coin.signTransaction(
        CoinType.Ethereum,
        result,
        '',
        ethereumParameter
      );

      console.log(`run 5 ${tx}`);

      let txRecipient = await provider.broadcastTransaction(`0x${tx}`);

      console.log(`hash = ${txRecipient.blockHash}`);

      console.log(bigintToHexString(chainId));
    } catch (error) {
      console.log(`receive error ${error}`);
    }
  }

  return (
    <View style={styles.container}>
      <Text>Mnemonic: {result}</Text>
      <Button title="Validate" onPress={validate} />

      <Button title="Sign Transaction" onPress={signTransaction} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
