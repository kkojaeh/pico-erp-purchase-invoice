package pico.erp.purchase.invoice.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Lazy
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.purchase.invoice.*
import pico.erp.purchase.order.PurchaseOrderId
import pico.erp.purchase.order.PurchaseOrderRequests
import pico.erp.purchase.order.PurchaseOrderService
import pico.erp.purchase.order.item.PurchaseOrderItemId
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

@SpringBootTest(classes = [PurchaseInvoiceApplication, TestConfig])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class PurchaseInvoiceItemServiceSpec extends Specification {

  @Lazy
  @Autowired
  PurchaseInvoiceService invoiceService

  @Lazy
  @Autowired
  PurchaseInvoiceItemService invoiceItemService

  @Lazy
  @Autowired
  PurchaseOrderService orderService

  def invoiceId = PurchaseInvoiceId.from("purchase-invoice-test")

  def id = PurchaseInvoiceItemId.from("purchase-invoice-item-1")

  def orderItemId = PurchaseOrderItemId.from("purchase-order-a-1")

  def unknownId = PurchaseInvoiceItemId.from("unknown")

  def orderId = PurchaseOrderId.from("purchase-order-a")

  def setup() {
    orderService.determine(
      new PurchaseOrderRequests.DetermineRequest(
        id: orderId
      )
    )
    orderService.send(
      new PurchaseOrderRequests.SendRequest(
        id: orderId
      )
    )
  }

  def cancelInvoice() {
    invoiceService.cancel(
      new PurchaseInvoiceRequests.CancelRequest(
        id: invoiceId
      )
    )
  }

  def determineInvoice() {
    invoiceService.determine(
      new PurchaseInvoiceRequests.DetermineRequest(
        id: invoiceId
      )
    )
  }

  def receiveInvoice() {
    invoiceService.receive(
      new PurchaseInvoiceRequests.ReceiveRequest(
        id: invoiceId
      )
    )
  }

  def createItem() {
    invoiceItemService.create(
      new PurchaseInvoiceItemRequests.CreateRequest(
        id: id,
        invoiceId: invoiceId,
        orderItemId: orderItemId,
        quantity: 100,
        remark: "품목 비고"
      )
    )
  }

  def createItem2() {
    invoiceItemService.create(
      new PurchaseInvoiceItemRequests.CreateRequest(
        id: PurchaseInvoiceItemId.from("purchase-invoice-item-2"),
        invoiceId: invoiceId,
        orderItemId: orderItemId,
        quantity: 100,
        remark: "품목 비고"
      )
    )
  }

  def updateItem() {
    invoiceItemService.update(
      new PurchaseInvoiceItemRequests.UpdateRequest(
        id: id,
        quantity: 200,
        remark: "품목 비고2",
      )
    )
  }

  def deleteItem() {
    invoiceItemService.delete(
      new PurchaseInvoiceItemRequests.DeleteRequest(
        id: id
      )
    )
  }


  def "존재 - 아이디로 확인"() {
    when:
    createItem()
    def exists = invoiceItemService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = invoiceItemService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    createItem()
    def item = invoiceItemService.get(id)
    then:
    item.id == id
    item.orderItemId == orderItemId
    item.invoiceId == invoiceId
    item.quantity == 100
    item.remark == "품목 비고"

  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    invoiceItemService.get(unknownId)

    then:
    thrown(PurchaseInvoiceItemExceptions.NotFoundException)
  }

  def "생성 - 작성 후 생성"() {
    when:
    createItem()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() > 0
  }

  def "생성 - 확정 후 생성"() {
    when:
    createItem()
    determineInvoice()
    createItem2()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() == 2

  }


  def "생성 - 취소 후 생성"() {
    when:
    createItem()
    cancelInvoice()
    createItem2()
    then:
    thrown(PurchaseInvoiceItemExceptions.CannotCreateException)
  }


  def "생성 - 수령 후 생성"() {
    when:
    createItem()
    determineInvoice()
    receiveInvoice()
    createItem2()
    then:
    thrown(PurchaseInvoiceItemExceptions.CannotCreateException)
  }

  def "수정 - 작성 후 수정"() {
    when:
    createItem()
    updateItem()
    def item = invoiceItemService.get(id)

    then:
    item.quantity == 200
    item.remark == "품목 비고2"
  }

  def "수정 - 확정 후 수정"() {
    when:
    createItem()
    determineInvoice()
    updateItem()
    def item = invoiceItemService.get(id)

    then:
    item.quantity == 200
    item.remark == "품목 비고2"

  }

  def "수정 - 취소 후 수정"() {
    when:
    createItem()
    cancelInvoice()
    updateItem()
    then:
    thrown(PurchaseInvoiceItemExceptions.CannotUpdateException)
  }


  def "수정 - 수령 후 수정"() {
    when:
    createItem()
    determineInvoice()
    receiveInvoice()
    updateItem()
    then:
    thrown(PurchaseInvoiceItemExceptions.CannotUpdateException)
  }


  def "삭제 - 작성 후 삭제"() {
    when:
    createItem()
    deleteItem()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() == 0
  }

  def "삭제 - 확정 후 삭제"() {
    when:
    createItem()
    determineInvoice()
    deleteItem()
    def items = invoiceItemService.getAll(invoiceId)
    then:
    items.size() == 0

  }

  def "삭제 - 취소 후 삭제"() {
    when:
    createItem()
    cancelInvoice()
    deleteItem()
    then:
    thrown(PurchaseInvoiceItemExceptions.CannotDeleteException)
  }

  def "삭제 - 수령 후 삭제"() {
    when:
    createItem()
    determineInvoice()
    receiveInvoice()
    deleteItem()
    then:
    thrown(PurchaseInvoiceItemExceptions.CannotDeleteException)
  }


}
