# trust_wallet_core_react_native

A React Native wrapper around the Trust Wallet Core wallet library for Android and iOS.

## Installation

```sh
npm install trust_wallet_core_react_native
```

Config your git hub token in android -> grade


## Usage

```js
import { createMnemonic } from 'trust_wallet_core_react_native';

// ...

const mnemonic = await createMnemonic(128, "");

let ethereumTransaction =
        new CoinParameter.Ethereum.TransferEthereumTransaction(
          '0x0348bca5a16000',
          '0xeC552cFb5Ad7d7f8FB6aA5D832487Fcf1C2f04EB'
        );

let ethereumParameter =
        new CoinParameter.Ethereum.EthereumTransactionParameter(
          bigintToHexString(1),
          bigintToHexString(BigInt(1)),
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

console.log(`receive tx ${tx}`)
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
