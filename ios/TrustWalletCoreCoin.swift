@objc(TrustWalletCoreCoin)
class TrustWalletCoreCoin: NSObject {
    
    func convertDataToReactNativeResult(data : Data) -> [Int32]{
        let intCount = data.count / MemoryLayout<Int32>.size
        let intArrayFromData = data.withUnsafeBytes { ptr in
            Array(UnsafeBufferPointer<Int32>(start: ptr.bindMemory(to: Int32.self).baseAddress!, count: intCount))
        }

        return intArrayFromData
    }

    @objc
    func getAddress(_ mnemonic: String, passPharse : String, coinType : Int , resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let address = try coin.getAddress(mnemonic: mnemonic, passPharse: passPharse)
            
            resolve(address)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    @objc
    func getSeed(_ mnemonic: String, passPharse : String, coinType : Int , resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let seed = try coin.getSeed(mnemonic: mnemonic, passPharse: passPharse)
            
            let array = convertDataToReactNativeResult(data: seed)
            
            resolve(array)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    @objc
    func getHexPrivateKey(_ mnemonic: String, passPharse : String, coinType : Int , resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let privateKey = try coin.getHexPrivateKey(mnemonic: mnemonic, passPharse: passPharse)
            
            resolve(privateKey)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    @objc
    func getRawPrivateKey(_ mnemonic: String, passPharse : String, coinType : Int , resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let privateKey = try coin.getRawPrivateKey(mnemonic: mnemonic, passPharse: passPharse)
            
            let array = convertDataToReactNativeResult(data: privateKey)
            
            resolve(array)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    @objc
    func getHexPublicKey(_ mnemonic: String, passPharse : String, coinType : Int , resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let publicKey = try coin.getHexPulicKey(mnemonic: mnemonic, passPharse: passPharse)
            
            resolve(publicKey)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    @objc
    func getRawPublicKey(_ mnemonic: String, passPharse : String, coinType : Int , resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let publicKey = try coin.getRawPublicKey(mnemonic: mnemonic, passPharse: passPharse)
            
            let array = convertDataToReactNativeResult(data: publicKey)
            
            resolve(array)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    
    @objc
    func sign(_ mnemonic: String, passPharse : String, coinType : Int ,message : String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let sig = try coin.sign(mnemonic: mnemonic, passPharse: passPharse, message: message)
            
            resolve(sig)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    @objc
    func signTransaction(_ mnemonic: String, passPharse : String, coinType : Int , tx: Dictionary<String, Any> ,resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock){
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let tx = try coin.signTransaction(mnemonic: mnemonic, passPharse: passPharse, tx: tx)
            
            resolve(tx)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
    
    @objc
    func signTransactionWithPrivateKey(_ privateKey : String,coinType : Int , tx: Dictionary<String, Any> ,resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock){
        do {
            let coin = try Coin.createCoinByType(coinType: coinType)
            
            let tx = try coin.signTransaction(privateKey: privateKey, tx: tx)
            
            resolve(tx)
        }catch let error as CError{
            reject(error.toReactCode(),error.toReactException(),nil)
        }catch {
            reject(CError.Unknown.toReactCode(),error.localizedDescription,nil)
        }
    }
}
