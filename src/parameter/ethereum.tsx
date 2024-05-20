import { Parameter } from './parameter';

enum EthereumTransactionType {
  Transfer = 'Transfer',
  ERC20_Transfer = 'ERC20_Transfer',
  ERC721_Transfer = 'ERC721_Transfer',
  ERC20_Approve = 'ERC20_Approve',
}

abstract class EthereumTransaction {
  type: EthereumTransactionType;

  constructor(type: EthereumTransactionType) {
    this.type = type;
  }

  abstract toNativeMap(): Object;
}

class TransferEthereumTransaction extends EthereumTransaction {
  amount: String;
  toAddress: String;

  constructor(amount: String, toAddress: String) {
    super(EthereumTransactionType.Transfer);
    this.amount = amount;
    this.toAddress = toAddress;
  }

  toNativeMap(): Object {
    let map = {
      type: this.type.valueOf(),
      amount: this.amount,
      to: this.toAddress,
    };

    return map;
  }
}

class ERC20TransferEthereumTransaction extends EthereumTransaction {
  amount: String;
  toAddress: String;
  contractAddress: String;

  constructor(amount: String, toAddress: String, contractAddress: String) {
    super(EthereumTransactionType.ERC20_Transfer);
    this.amount = amount;
    this.contractAddress = contractAddress;
    this.toAddress = toAddress;
  }

  toNativeMap(): Object {
    let map = {
      type: this.type.valueOf(),
      amount: this.amount,
      to: this.toAddress,
      contractAddress: this.contractAddress,
    };

    return map;
  }
}

class ERC20ApproveEthereumTransaction extends EthereumTransaction {
  amount: String;
  spender: String;
  contractAddress: String;

  constructor(amount: String, spender: String, contractAddress: String) {
    super(EthereumTransactionType.ERC20_Approve);
    this.amount = amount;
    this.spender = spender;
    this.contractAddress = contractAddress;
  }

  toNativeMap(): Object {
    let map = {
      type: this.type.valueOf(),
      spender: this.spender,
      amount: this.amount,
      contractAddress: this.contractAddress,
    };

    return map;
  }
}

class ERC721TransferEthereumTransaction extends EthereumTransaction {
  tokenId: String;
  toAddress: String;
  contractAddress: String;

  constructor(tokenId: String, toAddress: String, contractAddress: String) {
    super(EthereumTransactionType.ERC721_Transfer);
    this.tokenId = tokenId;
    this.contractAddress = contractAddress;
    this.toAddress = toAddress;
  }

  toNativeMap(): Object {
    let map = {
      type: this.type.valueOf(),
      tokenId: this.tokenId,
      to: this.toAddress,
      contractAddress: this.contractAddress,
    };

    return map;
  }
}

class EthereumTransactionParameter extends Parameter<EthereumTransaction> {
  private chainId: String;
  private nonce: String;
  private gasPrice: String;
  private gasLimit: String;

  constructor(
    chainId: String,
    nonce: String,
    gasPrice: String,
    gasLimit: String,
    transaction: EthereumTransaction
  ) {
    super(transaction);
    this.chainId = chainId;
    this.nonce = nonce;
    this.gasPrice = gasPrice;
    this.gasLimit = gasLimit;
  }

  toNativeMap(): Object {
    let map = {
      chainId: this.chainId,
      nonce: this.nonce,
      gasPrice: this.gasPrice,
      gasLimit: this.gasLimit,
      transaction: this.getTransaction()?.toNativeMap(),
    };

    return map;
  }
}

export {
  EthereumTransactionType,
  EthereumTransaction,
  TransferEthereumTransaction,
  ERC20TransferEthereumTransaction,
  ERC20ApproveEthereumTransaction,
  ERC721TransferEthereumTransaction,
  EthereumTransactionParameter,
};
