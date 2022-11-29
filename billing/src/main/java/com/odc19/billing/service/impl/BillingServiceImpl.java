package com.odc19.billing.service.impl;

import com.google.maps.model.LatLng;
import com.odc19.amqp.RabbitMQMessageProducer;
import com.odc19.billing.entity.BillEntity;
import com.odc19.billing.mapper.BillMapper;
import com.odc19.billing.repository.readRepository.BillingReadRepository;
import com.odc19.billing.repository.writeRepository.BillingWriteRepository;
import com.odc19.billing.service.iService.BillingService;
import com.odc19.clients.billing.BillDTO;
import com.odc19.clients.billing.BillDetailsDTO;
import com.odc19.clients.medicalShop.GoodsClient;
import com.odc19.clients.medicalShop.GoodsDTO;
import com.odc19.clients.medicalShop.MedicalShopClient;
import com.odc19.clients.medicalShop.MedicalShopDTO;
import com.odc19.clients.notification.NotificationDTO;
import com.odc19.clients.user.UserClient;
import com.odc19.clients.user.UserDTO;
import com.odc19.geocode.GeocodeService;
import com.odc19.typeofmessage.TypeOfMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(transactionManager = "transactionManagerWrite")
public class BillingServiceImpl implements BillingService {

    @Autowired
    BillingWriteRepository writeRepository;

    @Autowired
    BillingReadRepository readRepository;

    @Autowired
    BillMapper billMapper;

    @Autowired
    RabbitMQMessageProducer rabbitMQMessageProducer;

    @Autowired
    UserClient userClient;

    @Autowired
    GoodsClient goodsClient;

    @Autowired
    MedicalShopClient medicalShopClient;

    @Override
    public void saveNewBill(BillDTO billDTO) {
        if (!billDTO.isPaymentConfirmed()) {
            MedicalShopDTO medicalShopDTO = medicalShopClient.getMedicalShopById(billDTO.getMedicalShopId());
            UserDTO customerDTO = userClient.getUserById(billDTO.getCustomerId());
            UserDTO shipperDTO = getNearestShipperBasedOnMedicalShop(medicalShopDTO);
            publishMessageToQueue(sendBillToQueueShipper(billDTO, shipperDTO, medicalShopDTO, customerDTO));
            publishMessageToQueue(sendBillToQueueCustomer(billDTO, shipperDTO, customerDTO));
        }
        billDTO.setTotalPrice(Double.parseDouble(calculateTotalPriceOnBill(billDTO)));
        writeRepository.save(billMapper.dtoToEntity(billDTO));
    }

    @Override
    public BillDTO updateBill(BillDTO billDTO) {
        BillEntity oldBill = readRepository.findById(billDTO.getBillId()).orElseThrow(EntityNotFoundException::new);
        BillEntity newBill = billMapper.updateBillMapper(oldBill, billMapper.dtoToEntity(billDTO));
        if (newBill.isPaymentConfirmed()) {
            /*
             * func get the nearest shipper based on medicalLocation
             *
             * */
            MedicalShopDTO medicalShopDTO = medicalShopClient.getMedicalShopById(billDTO.getMedicalShopId());
            UserDTO shipperDTO = getNearestShipperBasedOnMedicalShop(medicalShopDTO);
            newBill.setShipperId(shipperDTO.getUserId());
            UserDTO customerDTO = userClient.getUserById(billDTO.getCustomerId());
            newBill.setDeliverToAddress(customerDTO.getStreetNumber() + " " + customerDTO.getAddress());

//            push to notification queue
            publishMessageToQueue(sendBillToQueueShipper(billDTO, shipperDTO, medicalShopDTO, customerDTO));
            publishMessageToQueue(sendBillToQueueCustomer(billDTO, shipperDTO, customerDTO));
        }
        newBill.setTotalPrice(Double.parseDouble(calculateTotalPriceOnBill(billDTO)));
        return billMapper.entityToDto(writeRepository.save(newBill));
    }

    @Override
    public List<BillDTO> getAll() {
        return readRepository.findAll()
                .stream()
                .map(billEntity -> billMapper.entityToDto(billEntity))
                .collect(Collectors.toList());
    }

    @Override
    public BillDTO getBillById(String billId) {
        return billMapper.entityToDto(readRepository.findById(billId).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public void deleteBill(String billId) {
        BillEntity billEntity = readRepository.findById(billId).orElseThrow(EntityNotFoundException::new);
        billEntity.setDeleted(true);
    }

    @Override
    public BillDTO recoverBillIsDeleted(String billId) {
        BillEntity billEntity = readRepository.findById(billId).orElseThrow(EntityNotFoundException::new);
        billEntity.setDeleted(false);
        return billMapper.entityToDto(billEntity);
    }

    @Override
    public List<BillDTO> getBillForShipper(String shipperId) {
        return readRepository
                .findBillEntitiesByShipperId(shipperId)
                .stream()
                .map(billEntity -> billMapper.entityToDto(billEntity))
                .collect(Collectors.toList());
    }

    @Override
    public BillDetailsDTO getBillDetailById(String billId) {
        BillDTO billDTO = billMapper.entityToDto(readRepository.findById(billId).orElseThrow(EntityNotFoundException::new));
        BillDetailsDTO billDetailsDTO = billMapper.convertBillDtoToBillDetails(billDTO);
        billDetailsDTO.setCustomerDTO(userClient.getUserById(billDTO.getCustomerId()));
        billDetailsDTO.setShipperDTO(userClient.getUserById(billDTO.getShipperId()));
        billDetailsDTO.setMedicalShopDTO(medicalShopClient.getMedicalShopById(billDTO.getMedicalShopId()));
        List<String> convertedGoodIdList = Stream.of(billDTO.getGoodsIdStringList().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = new ArrayList<>();
        for (String s : convertedGoodIdList) {
            GoodsDTO goodsDTO = medicalShopClient.getGoodsById(s);
            goodsDTOS.add(goodsDTO);
        }
        billDetailsDTO.setGoodsDTOS(goodsDTOS);
        return billDetailsDTO;
    }

    @Override
    public List<BillDetailsDTO> getBillDetailList() {
        List<BillEntity> billEntities = readRepository.findAll();
        List<BillDetailsDTO> billDetailsDTOS = new ArrayList<>();
        for (BillEntity billEntity : billEntities) {
            BillDTO billDTO = billMapper.entityToDto(billEntity);
            BillDetailsDTO billDetailsDTO = billMapper.convertBillDtoToBillDetails(billDTO);
            if (billDTO.getCustomerId() != null) {
                billDetailsDTO.setCustomerDTO(userClient.getUserById(billDTO.getCustomerId()));
            }
            if (billDTO.getShipperId() != null) {
                billDetailsDTO.setShipperDTO(userClient.getUserById(billDTO.getShipperId()));
            }
            if (billDTO.getMedicalShopId() != null) {
                billDetailsDTO.setMedicalShopDTO(medicalShopClient.getMedicalShopById(billDTO.getMedicalShopId()));
            }
            if (billDTO.getGoodsIdStringList() != null) {
                List<String> convertedGoodIdList = Stream.of(billDTO.getGoodsIdStringList().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
                List<GoodsDTO> goodsDTOS = new ArrayList<>();
                for (String s : convertedGoodIdList) {
                    GoodsDTO goodsDTO = medicalShopClient.getGoodsById(s);
                    goodsDTOS.add(goodsDTO);
                }
                billDetailsDTO.setGoodsDTOS(goodsDTOS);
            }

            billDetailsDTOS.add(billDetailsDTO);
        }
        return billDetailsDTOS;
    }

    @Override
    public List<BillDetailsDTO> getBillDetailByShipperId(String shipperId) {
        List<BillEntity> billEntities = readRepository.findBillEntitiesByShipperId(shipperId);
        List<BillDetailsDTO> billDetailsDTOS = new ArrayList<>();
        for (BillEntity billEntity : billEntities) {
            BillDTO billDTO = billMapper.entityToDto(billEntity);
            BillDetailsDTO billDetailsDTO = billMapper.convertBillDtoToBillDetails(billDTO);
            if (billDTO.getCustomerId() != null) {
                billDetailsDTO.setCustomerDTO(userClient.getUserById(billDTO.getCustomerId()));
            }
            if (billDTO.getShipperId() != null) {
                billDetailsDTO.setShipperDTO(userClient.getUserById(billDTO.getShipperId()));
            }
            if (billDTO.getMedicalShopId() != null) {
                billDetailsDTO.setMedicalShopDTO(medicalShopClient.getMedicalShopById(billDTO.getMedicalShopId()));
            }
            if (billDTO.getGoodsIdStringList() != null) {
                List<String> convertedGoodIdList = Stream.of(billDTO.getGoodsIdStringList().split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());
                List<GoodsDTO> goodsDTOS = new ArrayList<>();
                for (String s : convertedGoodIdList) {
                    GoodsDTO goodsDTO = medicalShopClient.getGoodsById(s);
                    goodsDTOS.add(goodsDTO);
                }
                billDetailsDTO.setGoodsDTOS(goodsDTOS);
            }

            billDetailsDTOS.add(billDetailsDTO);
        }
        return billDetailsDTOS;
    }

    @Override
    public void deliveredBillById(String billId) {
        BillEntity billEntity = readRepository.findById(billId).orElseThrow(EntityNotFoundException::new);
        billEntity.setDelivered(true);
        writeRepository.save(billEntity);
    }

    //    function to publish message to notification.queue
    private void publishMessageToQueue(Object payloadParam) {
        rabbitMQMessageProducer.publish(payloadParam, "internal.exchange", "internal.notification.routing-key");
    }

    private NotificationDTO sendBillToQueueShipper(BillDTO billDTO, UserDTO shipperDTO, MedicalShopDTO medicalShopDTO, UserDTO customerDTO) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setBillDTO(billDTO);
        notificationDTO.setToCustomerEmail(shipperDTO.getEmail());
        notificationDTO.setToCustomerId(shipperDTO.getUserId());
        notificationDTO.setTypeOfMessage(TypeOfMessage.SHIPPER_MAIL_CONFIRMED);
        notificationDTO.setShipperDTO(shipperDTO);
        notificationDTO.setMedicalShopDTO(medicalShopDTO);
        notificationDTO.setCustomerDTO(customerDTO);
        notificationDTO.setMessage("Bill sent to shipper" + shipperDTO.getEmail());
        notificationDTO.setSender("System");
        return notificationDTO;
    }

    private NotificationDTO sendBillToQueueCustomer(BillDTO billDTO, UserDTO shipperDTO, UserDTO customerDTO) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setBillDTO(billDTO);
        notificationDTO.setToCustomerEmail(customerDTO.getEmail());
        notificationDTO.setToCustomerId(customerDTO.getUserId());
        notificationDTO.setTypeOfMessage(TypeOfMessage.BILL_PAYMENT_CONFIRMED);
        notificationDTO.setCustomerDTO(customerDTO);
        notificationDTO.setShipperDTO(shipperDTO);
        notificationDTO.setMessage("Bill paid successfully: " + billDTO.getBillId());
        notificationDTO.setSender("System");
        return notificationDTO;
    }

    private UserDTO getNearestShipperBasedOnMedicalShop(MedicalShopDTO medicalShopDTO) {
        GeocodeService geocodeService = new GeocodeService();
        List<UserDTO> shipperList = userClient.getShipperList();
        LatLng latLng = new LatLng(medicalShopDTO.getLatitude(), medicalShopDTO.getLongitude());
        return geocodeService.findNearestShipperBasedOnMedicalShopLatitudeAndLongitude(latLng, shipperList);
    }

    private String calculateTotalPriceOnBill(BillDTO billDTO) {
        List<String> goodsIdList = Stream.of(billDTO.getGoodsIdStringList().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        double sumOfGoodsInBill = 0.0;
        for (String goodsId : goodsIdList) {
            sumOfGoodsInBill += Double.parseDouble(medicalShopClient.getGoodsById(goodsId).getPrice());
        }
        return String.valueOf(sumOfGoodsInBill);
    }
}
