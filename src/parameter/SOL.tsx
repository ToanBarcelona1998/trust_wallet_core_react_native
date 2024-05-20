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

  abstract toNativeMap(): Map<String, any>;
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

  toNativeMap(): Map<String, any> {
    let map = new Map();

    map.set('type', this.type.valueOf());

    map.set('recipient', this.recipient);

    map.set('memo', this.memo);

    map.set('value', this.value);

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

  toNativeMap(): Map<String, any> {
    let map = new Map();

    map.set('type', this.type.valueOf());

    map.set('value', this.value);

    map.set('pubkey', this.validatorPubkey);

    map.set('account', this.stakeAccount);

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

  toNativeMap(): Map<String, any> {
    let map = new Map();

    map.set('type', this.type.valueOf());

    map.set('tokenAddress', this.tokenAddress);

    map.set('sender', this.sender);

    map.set('recipient', this.recipientAddress);

    map.set('memo', this.memo);

    map.set('amount', this.amount);

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

  toNativeMap(): Map<String, any> {
    let map = new Map();

    map.set('transaction', this.getTransaction()?.toNativeMap());

    map.set('sender', this.sender);

    map.set('nonce', this.nonce);

    map.set('recentBlockHash', this.recentBlockHash);

    map.set('feePayerPrivateKey', this.feePayerPrivateKey);

    map.set('feePayer', this.feePayer);

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
