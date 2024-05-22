//
//  ICoin.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation

protocol ICoin{
    func getAddress(mnemonic : String, passPharse : String) throws -> String
    
    func getSeed(mnemonic : String , passPharse : String) throws -> Data
    
    func getHexPrivateKey(mnemonic : String, passPharse : String) throws -> String
    
    func getRawPrivateKey(mnemonic : String , passPharse : String) throws -> Data
    
    func getHexPulicKey(mnemonic : String , passPharse : String) throws -> String
    
    func getRawPublicKey(mnemonic : String , passPharse : String) throws -> Data
    
    func sign(mnemonic : String, passPharse : String , message : String) throws -> String
    
    func signTransaction(mnemonic : String, passPharse : String , tx : Dictionary<String,Any>) throws -> String
    
    func signTransaction(privateKey : String , tx : Dictionary<String,Any>) throws -> String
}
