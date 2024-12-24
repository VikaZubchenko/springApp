package pz.sptingapp.dao;

import java.sql.SQLException;

public class DAOFactory {
    private static IMyDAO dao;

    public static IMyDAO getDAOInstance(TypeDAO type) throws SQLException {
        IMyDAO dao = null;
        if (type == TypeDAO.MySQL) {
            dao = new MySQLDAO();
        }
        if (type == TypeDAO.MySQL) {
            dao = new CollectionLDAO();
        }
        return dao;
    }
}

