package pico.erp.purchase.invoice.item;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.purchase.invoice.PurchaseInvoiceId;

@Repository
interface PurchaseInvoiceItemEntityRepository extends
  CrudRepository<PurchaseInvoiceItemEntity, PurchaseInvoiceItemId> {

  @Query("SELECT i FROM PurchaseInvoiceItem i WHERE i.invoiceId = :invoiceId ORDER BY i.createdDate")
  Stream<PurchaseInvoiceItemEntity> findAllBy(@Param("invoiceId") PurchaseInvoiceId planId);

}

@Repository
@Transactional
public class PurchaseInvoiceItemRepositoryJpa implements PurchaseInvoiceItemRepository {

  @Autowired
  private PurchaseInvoiceItemEntityRepository repository;

  @Autowired
  private PurchaseInvoiceItemMapper mapper;

  @Override
  public PurchaseInvoiceItem create(PurchaseInvoiceItem planItem) {
    val entity = mapper.jpa(planItem);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(PurchaseInvoiceItemId id) {
    repository.deleteById(id);
  }

  @Override
  public boolean exists(PurchaseInvoiceItemId id) {
    return repository.existsById(id);
  }

  @Override
  public Stream<PurchaseInvoiceItem> findAllBy(PurchaseInvoiceId planId) {
    return repository.findAllBy(planId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<PurchaseInvoiceItem> findBy(PurchaseInvoiceItemId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public void update(PurchaseInvoiceItem planItem) {
    val entity = repository.findById(planItem.getId()).get();
    mapper.pass(mapper.jpa(planItem), entity);
    repository.save(entity);
  }
}
