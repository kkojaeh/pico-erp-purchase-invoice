package pico.erp.purchase.invoice

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Lazy
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.invoice.InvoiceRequests
import pico.erp.invoice.InvoiceService
import pico.erp.purchase.invoice.item.PurchaseInvoiceItemService
import pico.erp.purchase.order.PurchaseOrderId
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.user.UserId
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest(classes = [PurchaseInvoiceApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class PurchaseInvoiceServiceSpec extends Specification {

  @Autowired
  PurchaseInvoiceService purchaseInvoiceService

  @Autowired
  PurchaseInvoiceItemService invoiceItemService

  @Lazy
  @Autowired
  InvoiceService invoiceService

  def id = PurchaseInvoiceId.from("purchase-invoice-1")

  def id2 = PurchaseInvoiceId.from("purchase-invoice-2")

  def unknownId = PurchaseInvoiceId.from("unknown")

  def dueDate = LocalDateTime.now().plusDays(7)

  def remark = "요청 비고"

  def dueDate2 = LocalDateTime.now().plusDays(8)

  def orderId = PurchaseOrderId.from("purchase-order-b")

  def remark2 = "요청 비고2"

  def confirmerId = UserId.from("kjh")


  def setup() {
    purchaseInvoiceService.create(
      new PurchaseInvoiceRequests.CreateRequest(
        id: id,
        orderId: orderId,
        dueDate: dueDate,
        remark: remark
      )
    )
  }

  def createInvoice2() {
    purchaseInvoiceService.create(
      new PurchaseInvoiceRequests.CreateRequest(
        id: id2,
        orderId: orderId,
        dueDate: dueDate,
        remark: remark
      )
    )
  }

  def cancelInvoice() {
    purchaseInvoiceService.cancel(
      new PurchaseInvoiceRequests.CancelRequest(
        id: id
      )
    )
  }

  def determineInvoice() {
    purchaseInvoiceService.determine(
      new PurchaseInvoiceRequests.DetermineRequest(
        id: id
      )
    )
  }


  def receiveInvoice() {
    purchaseInvoiceService.receive(
      new PurchaseInvoiceRequests.ReceiveRequest(
        id: id
      )
    )
  }


  def updateInvoice() {
    purchaseInvoiceService.update(
      new PurchaseInvoiceRequests.UpdateRequest(
        id: id,
        dueDate: dueDate2,
        remark: remark2
      )
    )
  }


  def receiveInvoiceBy() {
    def invoice = purchaseInvoiceService.get(id)
    invoiceService.receive(
      new InvoiceRequests.ReceiveRequest(
        id: invoice.invoiceId,
        confirmerId: confirmerId
      )
    )
  }

  def "자동생성 - 발주를 통해 자동 생성"() {
    when:
    determineInvoice()
    def id = PurchaseInvoiceId.from("purchase-invoice-generated")
    def generated = purchaseInvoiceService.generate(
      new PurchaseInvoiceRequests.GenerateRequest(
        id: id,
        orderId: orderId
      )
    )
    def invoice = purchaseInvoiceService.get(generated.id)
    def items = invoiceItemService.getAll(generated.id)
    then:
    generated.id == id
    invoice.orderId == orderId
    items.size() == 2

  }

  def "생성 - 작성중인 송장 존재"() {
    when:
    createInvoice2()
    then:
    thrown(PurchaseInvoiceExceptions.DraftAlreadyExistsException)

  }

  def "생성 - 확정 한 송장 존재"() {
    when:
    determineInvoice()
    createInvoice2()
    def invoice = purchaseInvoiceService.get(id2)
    then:
    invoice.orderId == orderId

  }


  def "존재 - 아이디로 존재 확인"() {
    when:
    def exists = purchaseInvoiceService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = purchaseInvoiceService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def invoice = purchaseInvoiceService.get(id)

    then:
    invoice.id == id
    invoice.remark == remark
    invoice.dueDate == dueDate
    invoice.orderId == orderId

  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    purchaseInvoiceService.get(unknownId)

    then:
    thrown(PurchaseInvoiceExceptions.NotFoundException)
  }


  def "수정 - 취소 후 수정"() {
    when:
    cancelInvoice()
    updateInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotUpdateException)
  }


  def "수정 - 확정 후 수정"() {
    when:
    determineInvoice()
    updateInvoice()
    def invoice = purchaseInvoiceService.get(id)
    then:
    invoice.dueDate == dueDate2
    invoice.remark == remark2
  }

  def "수정 - 수령 후 수정"() {
    when:
    determineInvoice()
    receiveInvoice()
    updateInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotUpdateException)
  }


  def "수정 - 작성 후 수정"() {
    when:
    updateInvoice()
    def invoice = purchaseInvoiceService.get(id)

    then:
    invoice.dueDate == dueDate2
    invoice.remark == remark2
  }

  def "확정 - 작성 후 확정"() {
    when:
    determineInvoice()
    def invoice = purchaseInvoiceService.get(id)
    then:
    invoice.status == PurchaseInvoiceStatusKind.DETERMINED
    invoice.invoiceId != null
  }

  def "확정 - 확정 후 확정"() {
    when:
    determineInvoice()
    determineInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotDetermineException)
  }


  def "확정 - 취소 후 확정"() {
    when:
    cancelInvoice()
    determineInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotDetermineException)
  }

  def "확정 - 수령 후 확정"() {
    when:
    determineInvoice()
    receiveInvoice()
    determineInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotDetermineException)
  }

  def "취소 - 취소 후에는 취소"() {
    when:
    cancelInvoice()
    cancelInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotCancelException)
  }

  def "취소 - 확정 후 취소"() {
    when:
    determineInvoice()
    cancelInvoice()
    def invoice = purchaseInvoiceService.get(id)
    then:
    invoice.status == PurchaseInvoiceStatusKind.CANCELED
  }


  def "취소 - 수령 후 취소"() {
    when:
    determineInvoice()
    receiveInvoice()
    cancelInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotCancelException)
  }

  def "수령 - 작성 후 수령"() {
    when:
    receiveInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotReceiveException)
  }

  def "수령 - 확정 후 수령"() {
    when:
    determineInvoice()
    receiveInvoiceBy()
    def invoice = purchaseInvoiceService.get(id)
    then:
    invoice.status == PurchaseInvoiceStatusKind.RECEIVED

  }


  def "수령 - 취소 후 수령"() {
    when:
    cancelInvoice()
    receiveInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotReceiveException)
  }


  def "수령 - 수령 후 수령"() {
    when:
    determineInvoice()
    receiveInvoice()
    receiveInvoice()
    then:
    thrown(PurchaseInvoiceExceptions.CannotReceiveException)
  }


}
