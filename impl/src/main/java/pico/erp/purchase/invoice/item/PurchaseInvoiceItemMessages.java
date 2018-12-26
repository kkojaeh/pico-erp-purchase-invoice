package pico.erp.purchase.invoice.item;

import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.invoice.item.InvoiceItemData;
import pico.erp.item.ItemData;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.project.ProjectData;
import pico.erp.purchase.invoice.PurchaseInvoice;
import pico.erp.purchase.order.item.PurchaseOrderItemData;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;

public interface PurchaseInvoiceItemMessages {

  interface Create {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    class Request {

      @Valid
      @NotNull
      PurchaseInvoiceItemId id;

      @NotNull
      PurchaseInvoice invoice;

      @NotNull
      PurchaseOrderItemData orderItem;


      @NotNull
      @Min(0)
      BigDecimal quantity;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }


  interface Update {

    @Data
    class Request {

      @NotNull
      @Min(0)
      BigDecimal quantity;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }

  interface Delete {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Invoice {

    @Data
    class Request {

      InvoiceItemData invoiceItem;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

}
