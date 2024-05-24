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
      var provider = new ethers.JsonRpcProvider(
        'https://ethereum-sepolia-rpc.publicnode.com'
      );

      let address = await Coin.getAddress(CoinType.Ethereum, result, '');

      let nonce = await provider.getTransactionCount(`${address}`);

      let chainId = (await provider.getNetwork()).chainId;

      let ethereumTransaction =
        new CoinParameter.Ethereum.TransferEthereumTransaction(
          '0x0348bca5a16000',
          '0xeC552cFb5Ad7d7f8FB6aA5D832487Fcf1C2f04EB'
        );

      let ethereumParameter =
        new CoinParameter.Ethereum.EthereumTransactionParameter(
          bigintToHexString(chainId),
          bigintToHexString(BigInt(nonce)),
          '0x07FF684650',
          '0x5208',
          ethereumTransaction
        );

      let tx = await Coin.signTransaction(
        CoinType.Ethereum,
        result,
        '',
        ethereumParameter
      );

      let txRecipient = await provider.broadcastTransaction(`0x${tx}`);

      console.log(`receive tx hash ${txRecipient.hash}`);
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
