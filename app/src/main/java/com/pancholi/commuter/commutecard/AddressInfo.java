package com.pancholi.commuter.commutecard;

public class AddressInfo {

  private String address;
  private String addressId;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  public void clearValues() {
    address = null;
    addressId = null;
  }
}
