package com.pos.leaders.leaderspossystem.syncposservice.Enums;

/**
 * Created by KARAM on 08/08/2017.
 */

public interface MessageType{
    String Setup = "setup";
    String MessageType = "MessageType";

    String ADD_CLUB = "AddClub";
    String UPDATE_CLUB = "UpdateClub";
    String DELETE_CLUB = "DeleteClub";

    String ADD_CUSTOMER = "AddCustomer";
    String UPDATE_CUSTOMER = "UpdateCustomer";
    String DELETE_CUSTOMER = "DeleteCustomer";

    String ADD_PRODUCT = "AddProduct";
    String UPDATE_PRODUCT = "UpdateProduct";
    String DELETE_PRODUCT = "DeleteProduct";

    String ADD_USER = "AddUser";
    String UPDATE_USER = "UpdateUser";
    String DELETE_USER = "DeleteUser";

    String ADD_ORDER = "AddOrder";
    String UPDATE_ORDER = "UpdateOrder";
    String DELETE_ORDER = "DeleteOrder";
}
