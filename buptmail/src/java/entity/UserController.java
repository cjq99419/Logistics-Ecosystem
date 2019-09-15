package entity;

import entity.util.JsfUtil;
import entity.util.PaginationHelper;
import session.UserFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    private User current;
    private DataModel items = null;
    @EJB
    private session.UserFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    static private boolean login = false;
    String currenttel=null;

    public UserController() {
    }

    public User getSelected() {
        if (current == null) {
            current = new User();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UserFacade getFacade() {
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
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new User();
        selectedItemIndex = -1;
        return "Create";
    }

    public String resetLogin() {
        login = false;
        return "index";
    }

    public String isLogin() {
        if (login) {
            return "Usercenter.xhtml";
        } else {
            return null;
        }
    }

    static public boolean returnLogin() {
        return login;
    }

    public String login() {
        if (StaffController.returnLogin()) {
            JsfUtil.addSuccessMessage("您已经登录");
            return "PoseterCenter";
        }
        if (login) {
            JsfUtil.addSuccessMessage("您已经登录");
            return "Usercenter";
        }
        User user = new User();
        try {
            if (current != null) {
                String sql1 = "select * from user where tel=" + current.getTel();
                Query query;
                query = getFacade().getEntityManager().createNativeQuery(sql1, User.class);
                user = (User) query.getSingleResult(); //获取单个结果
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("用户不存在");
            return null;
        }
        try {
            if (current != null) {
                String sql2 = "select * from user where tel=" + current.getTel() + " and password=" + current.getPassword();
                Query query;
                query = getFacade().getEntityManager().createNativeQuery(sql2, User.class);
                user = (User) query.getSingleResult(); //获取单个结果               
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("loginsuccessfully"));

                login = true;
                currenttel= current.getTel();

                return "index";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("密码错误");
            return null;
        }
        return null;
    }

    public String getUser() {
        User user = new User();
        try {
            if (current != null) {
                String sql1 = "select * from user where tel=" + currenttel;
                Query query;
                query = getFacade().getEntityManager().createNativeQuery(sql1, User.class);
                user = (User) query.getSingleResult();
                current = user;
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
        return null;

    }

    public String create() {
        String tel = current.getTel();

        if (tel.length() != 11) {
            JsfUtil.addErrorMessage("手机号码位数不正确");
            return null;
        }

        if (Pattern.matches("^[1]([3]|[5]|[7]|[8])[0-9]{9}$", tel) == false) {
            JsfUtil.addErrorMessage("手机号码格式不正确");
            return null;
        }

        if (current.getPassword().length() > 12) {
            JsfUtil.addErrorMessage("密码至多12位");
            return null;
        }

        try {
            getFacade().create(current);
            JsfUtil.addErrorMessage("注册成功");
            return "index";
        } catch (Exception e) {
            JsfUtil.addErrorMessage("用户已存在");
            return null;
        }
    }

    public  String updateuser(){
     try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
            return "Usercenter";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String prepareEdit() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (User) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
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

    public User getUser(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + User.class.getName());
            }
        }

    }

}
