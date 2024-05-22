//
//  TrustWalletCoreMnemonic.mm
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 22/5/24.
//
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TrustWalletCoreMnemonic, NSObject)

RCT_EXTERN_METHOD(createMnemonic:(NSInteger *)strength passPharse:(NSString *)passPharse
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(validateMnemonic:(NSString *)mnemonic passPharse:(NSString *)passPharse
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
