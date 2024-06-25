export abstract class Parameter<T> {
  private transaction: T | undefined;
  constructor(transaction: T | undefined) {
    this.transaction = transaction;
  }

  getTransaction(): T | undefined {
    return this.transaction;
  }

  abstract toNativeMap(): Object;
}
