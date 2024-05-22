//
//  TrustWalletCoreMnemonic.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation

@objc(TrustWalletCoreMnemonic)
class TrustWalletCoreMnemonic : NSObject{
    
    @objc
    func createMnemonic(_ strength : Int32 , passPharse : String , resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock ){
        
        do {
            let mnemonic = try Mnemonic.random(strength: strength, passPharse: passPharse)
            
            resolve(mnemonic)
        }catch let error as CError{
            reject(nil,error.toReactException(),nil)
        }catch {
            reject(nil,CError.Unknown.toReactException(),nil)
        }
    }
    
    @objc
    func validateMnemonic(_ mnemonic : String , passPharse : String , resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock){
        let isValid = Mnemonic.validateMnemonic(mnemonic: mnemonic, passPharse: passPharse)
        
        resolve(isValid)
    }
}
