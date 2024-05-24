import { Parameter } from './parameter';

enum SOLTransactionType {
  Transfer = 'Transfer',
  Delegate_Stake = 'Delegate_Stake',
  Transfer_Token = 'Transfer_Token',
}

abstract class SOLTransaction {
  type: SOLTransactionType;

  constructor(type: SOLTransactionType) {
    this.type = type;
  }

  abstract toNativeMap(): Object;
}

class TransferSOLTransaction extends SOLTransaction {
  recipient: String;
  memo: String;
  value: Number;

  constructor(recipient: String, memo: String, value: Number) {
    super(SOLTransactionType.Transfer);
    this.recipient = recipient;
    this.memo = memo;
    this.value = value;
  }

  toNativeMap(): Object {
    let map = {
      type: this.type.valueOf(),
      recipient: this.recipient,
      memo: this.memo,
      value: this.value,
    };

    return map;
  }
}

class DelegateStakeSOLTransaction extends SOLTransaction {
  value: Number;
  validatorPubkey: String;
  stakeAccount: String;

  constructor(value: Number, validatorPubkey: String, stakeAccount: String) {
    super(SOLTransactionType.Transfer);
    this.value = value;
    this.validatorPubkey = validatorPubkey;
    this.stakeAccount = stakeAccount;
  }

  toNativeMap(): Object {
    let map = {
      type: this.type.valueOf(),
      value: this.value,
      pubkey: this.validatorPubkey,
      account: this.stakeAccount,
    };

    return map;
  }
}

class TransferTolenSOLTransaction extends SOLTransaction {
  tokenAddress: String;
  sender: String;
  recipientAddress: String;
  memo: String;
  amount: Number;

  constructor(
    tokenAddress: String,
    sender: String,
    recipientAddress: String,
    memo: String,
    amount: Number
  ) {
    super(SOLTransactionType.Transfer);
    this.tokenAddress = tokenAddress;
    this.sender = sender;
    this.recipientAddress = recipientAddress;
    this.memo = memo;
    this.amount = amount;
  }

  toNativeMap(): Object {
    let map = {
      type: this.type.valueOf(),
      tokenAddress: this.tokenAddress,
      sender: this.sender,
      recipient: this.recipientAddress,
      memo: this.memo,
      amount: this.amount,
    };

    return map;
  }
}

class SOLTransactionParameter extends Parameter<SOLTransaction> {
  sender: String;
  nonce: String;
  recentBlockHash: String;
  feePayerPrivateKey: String;
  feePayer: String;

  constructor(
    sender: String,
    nonce: String,
    recentBlockHash: String,
    feePayerPrivateKey: String,
    feePayer: String,
    transaction: SOLTransaction
  ) {
    super(transaction);
    this.sender = sender;
    this.nonce = nonce;
    this.recentBlockHash = recentBlockHash;
    this.feePayerPrivateKey = feePayerPrivateKey;
    this.feePayer = feePayer;
  }

  toNativeMap(): Object {
    let map = {
      transaction: this.getTransaction()?.toNativeMap(),
      sender: this.sender,
      nonce: this.nonce,
      recentBlockHash: this.recentBlockHash,
      feePayerPrivateKey: this.feePayerPrivateKey,
      feePayer: this.feePayer,
    };

    return map;
  }
}

export {
  SOLTransactionType,
  SOLTransaction,
  TransferSOLTransaction,
  DelegateStakeSOLTransaction,
  TransferTolenSOLTransaction,
  SOLTransactionParameter,
};
