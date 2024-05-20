import { NativeModules } from 'react-native';

const Mnemonic = NativeModules.TrustWalletCoreMnemonic
  ? NativeModules.TrustWalletCoreMnemonic
  : new Proxy(
      {},
      {
        get() {
          throw new Error("Mnemonic module doesn't exists");
        },
      }
    );

export function createMnemonic(
  length: Number,
  passPharse: String
): Promise<String> {
  return Mnemonic.createMnemonic(length, passPharse);
}

export function validateMnemonic(
  mnemonic: String,
  passPharse: String
): Promise<Boolean> {
  return Mnemonic.validateMnemonic(mnemonic, passPharse);
}
