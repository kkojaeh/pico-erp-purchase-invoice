package pico.erp.purchase.invoice.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.purchase.invoice.PurchaseInvoiceId
import pico.erp.purchase.invoice.PurchaseInvoiceRequests
import pico.erp.purchase.invoice.PurchaseInvoiceService
import pico.erp.purchase.order.item.PurchaseOrderItemId
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class PurchaseInvoiceItemServiceSpec extends Specification {

  @Autowired
  PurchaseInvoiceService orderService

  @Autowired
  PurchaseInvoiceItemService orderItemService

  def invoiceId = PurchaseInvoiceId.from("purchase-invoice-test")

  def id = PurchaseInvoiceItemId.from("purchase-invoice-item-1")

  def orderItemId = PurchaseOrderItemId.from("purchase-order-a-1")

  def unknownId = PurchaseInvoiceItemId.from("unknown")

  def setup() {

  }

  def cancelInvoice() {
    orderService.cancel(
      new PurchaseInvoiceRequests.CancelRequest(
        id: invoiceId
      )
    )
  }

  def determineInvoice() {
    orderService.determine(
      new PurchaseInvoiceRequests.DetermineRequest(
        id: invoiceId
      )
    )
  }

  def receiveInvoice() {
    orderService.receive(
      new PurchaseInvoiceRequests.ReceiveRequest(
        id: invoiceId
      )
    )
  }

  def createItem() {
    orderItemService.create(
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
    orderItemService.create(
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
    orderItemService.update(
      new PurchaseInvoiceItemRequests.UpdateRequest(
        id: id,
        quantity: 200,
        remark: "품목 비고2",
      )
    )
  }

  def deleteItem() {
    orderItemService.delete(
      new PurchaseInvoiceItemRequests.DeleteRequest(
        id: id
      )
    )
  }


  def "존재 - 아이디로 확인"() {
    when:
    createItem()
    def exists = orderItemService.exists(id)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = orderItemService.exists(unknownId)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    createItem()
    def item = orderItemService.get(id)
    then:
    item.id == id
    item.orderItemId == orderItemId
    item.invoiceId == invoiceId
    item.quantity == 100
    item.remark == "품목 비고"

  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    orderItemService.get(unknownId)

    then:
    thrown(PurchaseInvoiceItemExceptions.NotFoundException)
  }

  def "생성 - 작성 후 생성"() {
    when:
    createItem()
    def items = orderItemService.getAll(invoiceId)
    then:
    items.size() > 0
  }

  def "생성 - 확정 후 생성"() {
    when:
    createItem()
    determineInvoice()
    createItem2()
    def items = orderItemService.getAll(invoiceId)
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
    def item = orderItemService.get(id)

    then:
    item.quantity == 200
    item.remark == "품목 비고2"
  }

  def "수정 - 확정 후 수정"() {
    when:
    createItem()
    determineInvoice()
    updateItem()
    def item = orderItemService.get(id)

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
    def items = orderItemService.getAll(invoiceId)
    then:
    items.size() == 0
  }

  def "삭제 - 확정 후 삭제"() {
    when:
    createItem()
    determineInvoice()
    deleteItem()
    def items = orderItemService.getAll(invoiceId)
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
