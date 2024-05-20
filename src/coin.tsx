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

export function getSeed(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String
): Promise<String> {
  return TrustWalletCoreCoin.getSeed(mnemonic, passPharse, coinType.valueOf());
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

export async function sign(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String,
  messageHash: Uint8Array
): Promise<Uint8Array> {
  let rawSig = await TrustWalletCoreCoin.sign(
    mnemonic,
    passPharse,
    coinType.valueOf(),
    messageHash
  );

  return new Uint8Array(rawSig);
}

export async function signTransaction<T>(
  coinType: CoinType,
  mnemonic: String,
  passPharse: String,
  txMap: IParameter.Parameter<T>
): Promise<Uint8Array> {
  let tx = await TrustWalletCoreCoin.signTransaction(
    mnemonic,
    passPharse,
    coinType.valueOf(),
    txMap.toNativeMap()
  );

  return new Uint8Array(tx);
}

export async function multiSignTransaction<T>(
  coinType: CoinType,
  txMap: IParameter.Parameter<T>,
  privateKeys: Array<Uint8Array>
): Promise<Array<Uint8Array>> {
  let txs: Array<Array<number>> =
    await TrustWalletCoreCoin.multiSignTransaction(
      coinType.valueOf(),
      txMap.toNativeMap(),
      privateKeys
    );

  return txs.map((tx) => new Uint8Array(tx));
}

export { CoinType, IParameter };
