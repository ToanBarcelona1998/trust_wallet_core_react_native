import { Parameter } from './parameter';

class BinanceTransaction {
  private denom: String;
  private amount: Number;
  private toAddress: String;

  constructor(denom: String, amount: Number, toAddress: String) {
    this.denom = denom;
    this.amount = amount;
    this.toAddress = toAddress;
  }

  toNativeMap(): Object {
    let map = {
      denom: this.denom,
      amount: this.amount,
      to: this.toAddress,
    };

    return map;
  }
}

export class BinanceTransactionParameter extends Parameter<BinanceTransaction> {
  private chainId: String;
  private accountNumber: Number;
  private sequence: Number;
  private source: Number;
  private memo: String;

  constructor(
    chainId: String,
    accountNumber: Number,
    sequence: Number,
    source: Number,
    memo: String,
    transaction: BinanceTransaction
  ) {
    super(transaction);
    this.chainId = chainId;
    this.accountNumber = accountNumber;
    this.sequence = sequence;
    this.source = source;
    this.memo = memo;
  }

  toNativeMap(): Object {
    let map = {
      transaction: this.getTransaction()?.toNativeMap,
      chainId: this.chainId,
      accountNumber: this.accountNumber,
      sequence: this.sequence,
      source: this.source,
      memo: this.memo,
    };

    return map;
  }
}
