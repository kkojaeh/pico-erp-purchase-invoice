package pico.erp.purchase.invoice;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.order.PurchaseOrderId;

@Repository
interface PurchaseInvoiceEntityRepository extends
  CrudRepository<PurchaseInvoiceEntity, PurchaseInvoiceId> {

  @Query("SELECT i FROM PurchaseInvoice i WHERE i.orderId = :orderId ORDER BY i.createdDate")
  Stream<PurchaseInvoiceEntity> findAllBy(@Param("orderId") PurchaseOrderId orderId);

  @Query("SELECT i FROM PurchaseInvoice i WHERE i.invoiceId = :invoiceId")
  PurchaseInvoiceEntity findBy(@Param("invoiceId") InvoiceId invoiceId);

  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM PurchaseInvoice i WHERE i.invoiceId = :invoiceId")
  boolean exists(@Param("invoiceId") InvoiceId invoiceId);

}

@Repository
@Transactional
public class PurchaseInvoiceRepositoryJpa implements PurchaseInvoiceRepository {

  @Autowired
  private PurchaseInvoiceEntityRepository repository;

  @Autowired
  private PurchaseInvoiceMapper mapper;

  @Override
  public PurchaseInvoice create(PurchaseInvoice plan) {
    val entity = mapper.jpa(plan);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(PurchaseInvoiceId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(PurchaseInvoiceId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(InvoiceId invoiceId) {
    return repository.exists(invoiceId);
  }

  @Override
  public Optional<PurchaseInvoice> findBy(PurchaseInvoiceId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public Optional<PurchaseInvoice> findBy(InvoiceId invoiceId) {
    return Optional.ofNullable(repository.findBy(invoiceId))
      .map(mapper::jpa);
  }

  @Override
  public Stream<PurchaseInvoice> findAllBy(PurchaseOrderId orderId) {
    return repository.findAllBy(orderId)
      .map(mapper::jpa);
  }

  @Override
  public void update(PurchaseInvoice plan) {
    val entity = repository.findOne(plan.getId());
    mapper.pass(mapper.jpa(plan), entity);
    repository.save(entity);
  }
}
