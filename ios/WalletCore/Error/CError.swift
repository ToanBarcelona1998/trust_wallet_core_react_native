//
//  CError.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation

enum CError : Error {
    case MissingArgumentError
    case InvalidHexArgumentError
    case UnSupportedCoin
    case InvalidMnemonic
    case Unknown
    case UnsupportedTransaction
}

extension CError {
    func toReactException() -> String{
        var error = ""
        
        switch self {
        case .MissingArgumentError:
            error = "Missing argument error"
            break
        case .InvalidHexArgumentError:
            error = "Invalid hex argument error"
        case .UnSupportedCoin:
            error = "Unsupported coin"
            break
        case .InvalidMnemonic:
            error = "Invalid mnemonic"
            break
        case .Unknown:
            error = "Unknown error"
            break
        case .UnsupportedTransaction:
            error = "UnSupported Transaction"
            break
        }
        
        return error
    }
    
    func toReactCode() -> String {
        var code = ""
        
        switch self {
        case .MissingArgumentError:
            code = "MissingArgumentError"
            break
        case .InvalidHexArgumentError:
            code = "InvalidHexArgumentError"
            break
        case .UnSupportedCoin:
            code = "UnSupportedCoin"
            break
        case .InvalidMnemonic:
            code = "InvalidMnemonic"
            break
        case .Unknown:
            code = "Unknown"
            break
        case .UnsupportedTransaction:
            code = "UnsupportedTransaction"
            break
        }
        
        return code
    }
}



