import { Parameter } from './parameter';

class CardanoTransaction {
  private amount: Number;
  private txHash: String;
  private index: Number;
  private sender: String;

  constructor(amount: Number, txHash: String, index: Number, sender: String) {
    this.amount = amount;
    this.txHash = txHash;
    this.index = index;
    this.sender = sender;
  }

  toNativeMap(): Object {
    let map = {
      txid: this.txHash,
      amount: this.amount,
      index: this.index,
      sender: this.sender,
    };

    return map;
  }
}

export class CardanoTransactionParameter extends Parameter<
  [CardanoTransaction]
> {
  private toAddress: String;
  private changeAddress: String;
  private amount: Number;
  private ttl: Number;

  constructor(
    toAddress: String,
    changeAddress: String,
    amount: Number,
    ttl: Number,
    transaction: undefined
  ) {
    super(transaction);
    this.toAddress = toAddress;
    this.amount = amount;
    this.changeAddress = changeAddress;
    this.ttl = ttl;
  }

  toNativeMap(): Object {
    let map = {
      utxos: this.getTransaction()?.map((e) => e.toNativeMap()),
      ttl: this.ttl,
      to: this.toAddress,
      changeAddress: this.changeAddress,
      amount: this.amount,
    };

    return map;
  }
}
