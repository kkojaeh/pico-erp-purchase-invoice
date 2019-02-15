package pico.erp.purchase.invoice.item;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface PurchaseInvoiceItemEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice-item.created";

    private PurchaseInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice-item.updated";

    private PurchaseInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice-item.deleted";

    private PurchaseInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class InvoicedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice-item.invoiced";

    private PurchaseInvoiceItemId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class GeneratedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice-item.generated";

    private List<PurchaseInvoiceItemId> ids;

    public String channel() {
      return CHANNEL;
    }

  }
}
