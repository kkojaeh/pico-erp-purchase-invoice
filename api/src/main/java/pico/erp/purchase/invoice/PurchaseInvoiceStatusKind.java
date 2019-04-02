package pico.erp.purchase.invoice;

import pico.erp.shared.data.LocalizedNameable;

public enum PurchaseInvoiceStatusKind implements LocalizedNameable {

  /**
   * 작성중
   */
  DRAFT,

  /**
   * 발행
   */
  DETERMINED,

  /**
   * 수령 완료
   */
  RECEIVED,

  /**
   * 취소됨
   */
  CANCELED;

  public boolean isCancelable() {
    return this == DRAFT || this == DETERMINED;
  }

  public boolean isDeterminable() {
    return this == DRAFT;
  }

  public boolean isInvoiceable() {
    return this == DETERMINED;
  }

  public boolean isReceivable() {
    return this == DETERMINED;
  }

  public boolean isUpdatable() {
    return this == DRAFT || this == DETERMINED;
  }

}
