#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TrustWalletCoreCoin, NSObject)

RCT_EXTERN_METHOD(getAddress:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getSeed:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getHexPrivateKey:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getRawPrivateKey:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getHexPublicKey:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getRawPublicKey:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(sign:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  message:(NSString *)message
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)


RCT_EXTERN_METHOD(signTransaction:(NSString *)mnemonic passPharse:(NSString *)passPharse coinType:(NSNumber *)coinType
                  tx:(NSDictionary *)tx
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(signTransactionWithPrivateKey:(NSString *)privateKey coinType:(NSNumber *)coinType
                  tx:(NSDictionary *)tx
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
