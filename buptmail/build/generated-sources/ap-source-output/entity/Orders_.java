package entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-09-08T13:06:48")
@StaticMetamodel(Orders.class)
public class Orders_ { 

    public static volatile SingularAttribute<Orders, Date> date;
    public static volatile SingularAttribute<Orders, String> senderAddress;
    public static volatile SingularAttribute<Orders, Integer> weight;
    public static volatile SingularAttribute<Orders, String> type;
    public static volatile SingularAttribute<Orders, String> senderTel;
    public static volatile SingularAttribute<Orders, String> senderName;
    public static volatile SingularAttribute<Orders, Integer> price;
    public static volatile SingularAttribute<Orders, String> recipientName;
    public static volatile SingularAttribute<Orders, String> location;
    public static volatile SingularAttribute<Orders, Integer> id;
    public static volatile SingularAttribute<Orders, String> recipientAddress;
    public static volatile SingularAttribute<Orders, String> modePayment;
    public static volatile SingularAttribute<Orders, String> recipientTel;
    public static volatile SingularAttribute<Orders, String> status;

}