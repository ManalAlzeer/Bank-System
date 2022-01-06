package com.example.demo.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserController {

    ArrayList<User> list = new ArrayList<>();
    int bankBalance = 1000000;

    @GetMapping("/users")
    public ArrayList<User> getAll(){
        return list;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable String id){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId().equals(id)){
                return ResponseEntity.status(200).body(list.get(i));
            }
        }
        return ResponseEntity.status(400).body("There is no user have that id");
    }

    @PostMapping("/users")
    public ResponseEntity create(@RequestBody User user){
        if(user.getId() == null || user.getName() == null || user.getPassword() == null || user.getEmail() == null){
            return ResponseEntity.status(400).body("Please send all information");
        }
        if(user.getPassword().length() <= 6){
            return ResponseEntity.status(400).body("Password should be more than 6 characters. ");
        }
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getName().equals(user.getName())){
                return ResponseEntity.status(400).body("This name is used before");
            }
        }
        list.add(user);
        return ResponseEntity.status(200).body("User created");
        }

    @PutMapping("/users/{id}")
    public ResponseEntity editUser(@PathVariable String id, @RequestBody User user){
        if(user.getPassword().length() <= 6){
            return ResponseEntity.status(400).body("Password should be more than 6 characters. ");
        }
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getName().equals(user.getName())){
                return ResponseEntity.status(400).body("This name is used before");
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId().equals(id)){
                list.set(i , user);
                return ResponseEntity.status(200).body("user updated");
            }
        }
        return ResponseEntity.status(400).body("Please send failed id");
    }

//    deposit
    @PutMapping("users/{id}/deposit")
    public ResponseEntity deposit(@PathVariable String id, @RequestBody User user) {
        int depositAmount = user.getBalance();
        User currentUser;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                currentUser = list.get(i);
                if(!currentUser.getPassword().equals(user.getPassword())){
                    return ResponseEntity.status(400).body("Your password Incorrect !");
                }
                currentUser.setBalance(currentUser.getBalance()+ depositAmount);
                return ResponseEntity.status(200).body("successfully, your have: " + currentUser.getBalance());
            }
        }
        return ResponseEntity.status(400).body("Try again.");
    }

//    withdraw
    @PutMapping("users/{id}/withdraw")
    public ResponseEntity withdraw(@PathVariable String id, @RequestBody User user) {
        int withdrawAmount = user.getBalance();
        User currentUser;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                currentUser = list.get(i);
                if(!currentUser.getPassword().equals(user.getPassword())){
                    return ResponseEntity.status(400).body("Your password Incorrect !");
                }
                currentUser.setBalance(currentUser.getBalance()- withdrawAmount);
                return ResponseEntity.status(200).body("successfully, your have: " + currentUser.getBalance());
            }
        }
        return ResponseEntity.status(400).body("Try again.");
    }

// loan Amount
    @PutMapping("/users/{id}/takeLoan")
    public ResponseEntity takeLoan(@PathVariable String id , @RequestBody User user) {
        int newLoanAmount = user.getLoanAmount();
        User currentUser;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId().equals(id) ){
                currentUser = list.get(i);
                if(!currentUser.getPassword().equals(user.getPassword())){
                    return ResponseEntity.status(400).body("Your password Incorrect !");
                }
                currentUser.setLoanAmount(currentUser.getLoanAmount() + newLoanAmount);
                bankBalance = bankBalance - newLoanAmount;
                return ResponseEntity.status(200).body("Add Loan successfully, your loanAmount: " + currentUser.getLoanAmount() +
                        " and bankBalance now: " + bankBalance);
            }
        }
        return ResponseEntity.status(400).body("Try again.");
    }

    @PutMapping("/users/{id}/reduceLoan")
    public ResponseEntity reduceLoan(@PathVariable String id, @RequestBody User user) {
        int balance = user.getBalance();
        User currentUser;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId().equals(id) ){
                currentUser = list.get(i);
                if(!currentUser.getPassword().equals(user.getPassword())){
                    return ResponseEntity.status(400).body("Your password Incorrect !");
                }
                currentUser.setBalance(currentUser.getBalance() - balance);
                currentUser.setLoanAmount(currentUser.getLoanAmount() - balance);
                bankBalance = bankBalance + balance;
                return ResponseEntity.status(200).body("Add Loan successfully, your loanAmount: " + currentUser.getLoanAmount() +
                        " and bankBalance now: " + bankBalance);
            }
        }
        return ResponseEntity.status(400).body("Try again.");
    }

//    delete account
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable String id){
        User currentUser;
        for (int i = 0; i <= list.size(); i++) {
            if(list.get(i).getId().equals(id)){
                currentUser = list.get(i);
                if (currentUser.getLoanAmount() != 0){
                    return ResponseEntity.status(400).body("Sorry you can't delete your account because you have" + currentUser.getLoanAmount() + "loanAmount.");
                }else {
                    bankBalance = bankBalance + currentUser.getBalance();
                    list.remove(i);
                    return ResponseEntity.status(200).body("User deleted, and the bank Balance = " + bankBalance);
                }
            }
        }
        return ResponseEntity.status(400).body("Please choose failed id");
    }


    }
