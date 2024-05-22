//
//  Extension.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation

extension Dictionary<String,Any> {
    func toJsonString() -> String? {
        guard let data = try? JSONSerialization.data(withJSONObject: self, options: .prettyPrinted) else {
            return nil
        }
        return String(data: data, encoding: String.Encoding.utf8)
    }
    
}
