package pico.erp.purchase.invoice;

import java.time.LocalDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import pico.erp.invoice.InvoiceData;
import pico.erp.purchase.order.PurchaseOrderData;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;

public interface PurchaseInvoiceMessages {

  interface Create {

    @Data
    class Request {

      @Valid
      @NotNull
      PurchaseInvoiceId id;

      @NotNull
      PurchaseOrderData order;

      @Future
      @NotNull
      LocalDateTime dueDate;

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

      @Future
      @NotNull
      LocalDateTime dueDate;

      @Size(max = TypeDefinitions.REMARK_LENGTH)
      String remark;

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }


  interface Determine {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }
  }

  interface Receive {

    @Data
    class Request {

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }

  interface Cancel {

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

      InvoiceData invoice;

    }

    @Value
    class Response {

      Collection<Event> events;

    }

  }


}
