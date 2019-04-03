package pico.erp.purchase.invoice;

import kkojaeh.spring.boot.component.ComponentBean;
import kkojaeh.spring.boot.component.SpringBootComponent;
import kkojaeh.spring.boot.component.SpringBootComponentBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pico.erp.ComponentDefinition;
import pico.erp.purchase.invoice.PurchaseInvoiceApi.Roles;
import pico.erp.shared.SharedConfiguration;
import pico.erp.shared.data.Role;

@Slf4j
@SpringBootComponent("purchase-invoice")
@EntityScan
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
@Import(value = {
  SharedConfiguration.class
})
public class PurchaseInvoiceApplication implements ComponentDefinition {

  public static void main(String[] args) {
    new SpringBootComponentBuilder()
      .component(PurchaseInvoiceApplication.class)
      .run(args);
  }

  @Override
  public Class<?> getComponentClass() {
    return PurchaseInvoiceApplication.class;
  }

  @Bean
  @ComponentBean(host = false)
  public Role purchaseInvoiceManager() {
    return Roles.PURCHASE_INVOICE_MANAGER;
  }

  @Bean
  @ComponentBean(host = false)
  public Role purchaseInvoicePublisher() {
    return Roles.PURCHASE_INVOICE_PUBLISHER;
  }

}
