package entity;

import entity.util.JsfUtil;
import entity.util.PaginationHelper;
import session.StaffFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

@Named("staffController")
@SessionScoped
public class StaffController implements Serializable {

    private Staff current;
    private DataModel items = null;
    @EJB
    private session.StaffFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    static private boolean login = false;
    String currenttel=null;    
    

    public StaffController() {
    }
    
    static public boolean returnLogin()
    {
        return login;
    }
    
     static public String resetLogin()
     {
         login=false;
         return "/index";
     }
     
    public String isLogin()
    {
        if(login) return "PosterCenter.xhtml";
        else return null;
    }

    public String login() {
         if(UserController.returnLogin())
        {
            JsfUtil.addSuccessMessage("您已经登录");
            return "Usercenter";
        }
        if(login) 
        {
            JsfUtil.addSuccessMessage("您已经登录");
            return "PosterCenter";
        }
        Staff staff = new Staff();
        try {
           if(current != null){
    
               String sql1 = "select * from staff where tel="+current.getTel();
            Query query;
                query = getFacade().getEntityManager().createNativeQuery(sql1, Staff.class);
            staff = (Staff) query.getSingleResult(); //获取单个结果
        }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("用户不存在");
            return null;
        }
         try {
           if(current != null){
            String sql2 = "select * from staff where tel="+current.getTel()+" and password="+current.getPassword();
            Query query;
                query = getFacade().getEntityManager().createNativeQuery(sql2, Staff.class);
            staff = (Staff) query.getSingleResult(); //获取单个结果               
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("loginsuccessfully"));
                
                login=true;
                currenttel=current.getTel();
                
             if(staff.getPosition().equals("s"))
             return "/PosterCenter.xhtml";  
             else if(staff.getPosition().equals("m"))
              return "/ManagerCenter.xhtml";
}
        } catch (Exception e) {
            JsfUtil.addErrorMessage("密码错误");
            return null;
        }
        return null;
    }

public String getStaff() {
        Staff staff = new Staff();
        try {
            if (current != null) {
                String sql1 = "select * from staff where tel =" + currenttel;
                Query query;
                query = getFacade().getEntityManager().createNativeQuery(sql1, Staff.class);
                staff = (Staff) query.getSingleResult(); 
                current = staff;
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return null;
    }

  public List getStaffOrders() {
        Orders order = new Orders();
        try {
            if (current != null) {
                
               String sql1 = "select * from orders where orders.sender_address ="+"'"+current.getAddressRegion()+"'";
                Query query;
                query = getFacade().getEntityManager().createNativeQuery(sql1, Orders.class);
                List<Orders> listOrders = new ArrayList<Orders>();
                listOrders = query.getResultList(); 
                return listOrders;
            }
        } catch (Exception e) {
             JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return null;
    }
    public List getOrders() {
        List<Orders> orders = getStaffOrders();
        return orders;
    }
    
    public Staff getSelected() {
        if (current == null) {
            current = new Staff();
            selectedItemIndex = -1;
        }
        return current;
    }

    private StaffFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Staff) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Staff();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StaffCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Staff) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StaffUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
      public String update2() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StaffUpdated"));
            return "PosterCenter";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }


    public String destroy() {
        current = (Staff) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StaffDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Staff getStaff(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Staff.class)
    public static class StaffControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StaffController controller = (StaffController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "staffController");
            return controller.getStaff(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Staff) {
                Staff o = (Staff) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Staff.class.getName());
            }
        }

    }

}
