package pico.erp.purchase.invoice;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class PurchaseInvoiceApi {

  public final static ApplicationId ID = ApplicationId.from("purchase-invoice");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    PURCHASE_INVOICE_PUBLISHER,
    PURCHASE_INVOICE_MANAGER;

    @Id
    @Getter
    private final String id = name();

  }
}
