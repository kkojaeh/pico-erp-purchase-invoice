package pico.erp.purchase.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.event.Event;

public interface PurchaseInvoiceEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.created";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ProgressedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.progressed";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeterminedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.determined";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.updated";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CanceledEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.canceled";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ReceivedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.received";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class SentEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.sent";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class RejectedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.rejected";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class InvoicedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.invoiced";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class GeneratedEvent implements Event {

    public final static String CHANNEL = "event.purchase-invoice.generated";

    private PurchaseInvoiceId id;

    public String channel() {
      return CHANNEL;
    }

  }


}
