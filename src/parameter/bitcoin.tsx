import { Parameter } from './parameter';

class BitcoinTransaction {
  private amount: Number;
  private script: String;
  private hash: String;
  private index: Number;

  constructor(amount: Number, script: String, hash: String, index: Number) {
    this.amount = amount;
    this.script = script;
    this.hash = hash;
    this.index = index;
  }

  toNativeMap(): Object {
    let map = {
      value: this.amount,
      script: this.script,
      txid: this.hash,
      index: this.index,
    };

    return map;
  }
}

export class BitcoinTransactionParameter extends Parameter<
  [BitcoinTransaction]
> {
  private amount: Number;
  private toAddress: String;
  private changeAddress: String;

  constructor(
    amount: Number,
    toAddress: String,
    changeAddress: String,
    transaction: [BitcoinTransaction]
  ) {
    super(transaction);
    this.amount = amount;
    this.changeAddress = changeAddress;
    this.toAddress = toAddress;
  }

  toNativeMap(): Object {
    let map = {
      amount: this.amount,
      changeAddress: this.changeAddress,
      to: this.toAddress,
      utxos: this.getTransaction()?.map((e) => e.toNativeMap()),
    };

    return map;
  }
}
