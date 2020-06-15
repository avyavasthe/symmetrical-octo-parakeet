package com.cristal.crypto.controllers;


import com.cristal.crypto.dto.ExchangeDTO;
import com.cristal.crypto.entities.Wallet;
import com.cristal.crypto.services.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Api(description = "Exchange pertaining operations")
@Controller
@RequestMapping("/api")
public class OperationController {
    @Autowired
    WalletService walletService;
    private static final Logger logger = LoggerFactory.getLogger(OperationController.class);
    /**
     *
     * @param id = ID from wallet
     * @param exchangeDTO = body with information for exchange
     * @requestparam quantity = Quantity of initial currency you want to trade
     * @return String with resulting balance of target currency
     * @throws NotFoundException
     */
    @ApiOperation(value = "Buy cryptocoins", response=String.class)
    @PostMapping("/wallets/buy/{id}")
    public ResponseEntity<String> buyCoin(@RequestBody ExchangeDTO exchangeDTO) throws Exception {
        walletService.buyCryptoCurrency(exchangeDTO);
        Wallet wallet = walletService.getWalletById(exchangeDTO.getWalletID());
        String response = "Your " + exchangeDTO.getExchangeTo().toUpperCase() + " balance: " + wallet.getBalance().get(exchangeDTO.getExchangeTo());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }
    /**
     *
     * @param idFrom = ID from wallet doing the transfer
     * @param idTo = ID from wallet receiving the transfer
     * @param coin = ID from exchange target currency
     * @param quantity = Quantity of initial currency you want to trade
     * @return String with new balance of the wallet receiving the transfer
     * @throws NotFoundException
     */

    //TODO change to DTO
    @ApiOperation(value = "Transfer from one wallet to another", response=String.class)
    @PostMapping("/wallets/transfer/{idFrom}/{idTo}/{coin}")
    public ResponseEntity<String > transfer(@PathVariable Long idFrom, @PathVariable Long idTo, @PathVariable String coin, @RequestParam String quantity) throws NotFoundException {
        Double q = Double.parseDouble(quantity);
        String response = walletService.transferCoins(idFrom, idTo, q, coin);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);

    }
}
