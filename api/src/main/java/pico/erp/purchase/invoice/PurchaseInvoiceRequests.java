package pico.erp.purchase.invoice;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;
import pico.erp.shared.TypeDefinitions;

public interface PurchaseInvoiceRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId id;

    @Valid
    @NotNull
    PurchaseOrderId orderId;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId id;

    @Future
    @NotNull
    OffsetDateTime dueDate;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DetermineRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class ReceiveRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId id;

    /*UserId confirmerId;*/

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CancelRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class InvoiceRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId id;

    @Valid
    @NotNull
    InvoiceId invoiceId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class GenerateRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId id;

    @Valid
    @NotNull
    PurchaseOrderId orderId;

  }

}
