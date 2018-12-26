package pico.erp.purchase.invoice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface PurchaseInvoiceExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.draft.already.exists.exception")
  class DraftAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.cannot.update.exception")
  class CannotUpdateException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.cannot.determine.exception")
  class CannotDetermineException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.cannot.send.exception")
  class CannotSendException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.cannot.cancel.exception")
  class CannotCancelException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.cannot.receive.exception")
  class CannotReceiveException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.cannot.invoice.exception")
  class CannotInvoiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }


  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "purchase-invoice.cannot.reject.exception")
  class CannotRejectException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "purchase-invoice.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
