# trust_wallet_core_react_native

A React Native wrapper around the Trust Wallet Core wallet library for Android and iOS.

## Installation

```sh
npm install trust_wallet_core_react_native
```

Config your git hub token in android -> grade


## Usage

The first step
1. With android
*** 
- Move to android folder -> MainApplication.kt
- Add this line into MainApplication

```kotlin
  init {
          System.loadLibrary("TrustWalletCore")
      }
```
***
2. With IOS
***
- Cd IOS folder
- Add this line `pod 'TrustWalletCore'` to Podfile
- Run pod install to
***

```js
import { Mnemonic, TrustWalletCore, CoinType, CoinParameter,} from 'trust_wallet_core_react_native';

// ...

const mnemonic = await Mnemonic.createMnemonic(128, "");


// Get address
let address = await TrustWalletCore.getAddress(
        CoinType.Ethereum,
        result,
        ''
      );

// Get private key
let privateKey = await TrustWalletCore.getHexPrivateKey(
  CoinType.Ethereum,
  result,
  ''
  )

// Create ethereum transaction
let ethereumTransaction =
        new CoinParameter.Ethereum.TransferEthereumTransaction(
          '0x0348bca5a16000',
          '0xeC552cFb5Ad7d7f8FB6aA5D832487Fcf1C2f04EB'
        );

// Create ethereum raw tx
let ethereumParameter =
        new CoinParameter.Ethereum.EthereumTransactionParameter(
          '0x79',
          '0x123',
          '0x07FF684650',
          '0x5208',
          ethereumTransaction
        );

// Sign transaction
let tx = await TrustWalletCore.signTransaction(
        CoinType.Ethereum,
        mnemonic,
        '',
        ethereumParameter
      );

console.log(`receive tx ${tx}`)
```

## Contributing

***
- For now there are no strict guidelines.
- Just create your pull request.
- Make sure to add a description what the pull request is about.
- If you have any questions feel free to ask.
- Looking forward contributions.
*** 

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
