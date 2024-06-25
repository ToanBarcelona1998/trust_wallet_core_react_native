import { CoinType } from './coin_type';
import { NativeModules } from 'react-native';
import * as IParameter from './parameter/index';

const TrustWalletCoreCoin = NativeModules.TrustWalletCoreCoin
  ? NativeModules.TrustWalletCoreCoin
  : new Proxy(
      {},
      {
        get() {
          throw new Error("TrustWalletCoreCoin module doesn't exists");
        },
      }
    );

export function getAddress(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String
): Promise<String> {
  return TrustWalletCoreCoin.getAddress(
    mnemonic,
    passPharse,
    coinType.valueOf()
  );
}

export async function getSeed(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String
): Promise<Uint8Array> {
  let seed = await TrustWalletCoreCoin.getSeed(
    mnemonic,
    passPharse,
    coinType.valueOf()
  );

  return new Uint8Array(seed);
}

export function getHexPrivateKey(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String
): Promise<String> {
  return TrustWalletCoreCoin.getHexPrivateKey(
    mnemonic,
    passPharse,
    coinType.valueOf()
  );
}

export async function getRawPrivateKey(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String
): Promise<Uint8Array> {
  let privateKey = await TrustWalletCoreCoin.getRawPrivateKey(
    mnemonic,
    passPharse,
    coinType.valueOf()
  );

  return new Uint8Array(privateKey);
}

export function getHexPublicKey(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String
): Promise<String> {
  return TrustWalletCoreCoin.getHexPublicKey(
    mnemonic,
    passPharse,
    coinType.valueOf()
  );
}

export async function getRawPublicKey(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String
): Promise<Uint8Array> {
  let publicKey = await TrustWalletCoreCoin.getRawPublicKey(
    mnemonic,
    passPharse,
    coinType.valueOf()
  );

  return new Uint8Array(publicKey);
}

export function sign(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String,
  message: String
): Promise<String> {
  return TrustWalletCoreCoin.sign(
    mnemonic,
    passPharse,
    coinType.valueOf(),
    message
  );
}

export async function signTransaction<T>(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String,
  txMap: IParameter.Parameter<T>
): Promise<String> {
  return TrustWalletCoreCoin.signTransaction(
    mnemonic,
    passPharse,
    coinType.valueOf(),
    txMap.toNativeMap()
  );
}

export async function signTransactionWithPrivateKey<T>(
  coinType: CoinType,
  privateKey: String,
  txMap: IParameter.Parameter<T>
): Promise<String> {
  return TrustWalletCoreCoin.signTransactionWithPrivateKey(
    privateKey,
    coinType.valueOf(),
    txMap.toNativeMap()
  );
}
