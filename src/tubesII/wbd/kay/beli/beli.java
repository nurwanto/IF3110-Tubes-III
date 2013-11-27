package tubesII.wbd.kay.beli;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class beli
 */
@WebServlet("/beli")
public class beli extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public beli() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String creditid = request.getParameter("creditid");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Connection con = null;
		Statement state = null;
		Statement state2 = null;
		ResultSet result = null;
		Vector<String> shopping_cart = (Vector<String>) session.getAttribute("shopping_cart");
		Vector<String> shopping_request = (Vector<String>) session.getAttribute("shopping_request");
		Vector<Integer> item= (Vector<Integer>) session.getAttribute("amount");
		
		if(creditid.equals("")){
			out.print("You haven't choose the credit card");
		}
		else{
			for(int i = 0; i< shopping_cart.size();i++){
				int checkAmount=0;
				try{
					String uname = "root";
					String pass = "";
					String url = "jdbc:mysql://localhost/progin_13511059";
					Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			        con = DriverManager.getConnection (url, uname, pass);
				}
				catch(Exception e){
					System.out.println("Cannot connect to database "+ e.getMessage());
					out.print("We're currently Unable to access the database. Please try again Later.");
				}
				try{
					state = con.createStatement();
					String regquery = "SELECT * FROM `progin_13511059`.barang WHERE id_barang ='"+shopping_cart.get(i)+"'";
					result = state.executeQuery(regquery);
					while(result.next()){
						if(item.get(i)> Integer.parseInt(result.getString(8))){
							out.print("The amount of item number "+(i+1)+" is invalid");
						}
						else{
							int newPembelian = Integer.parseInt(result.getString(6))+ item.get(i);
							int newStok = Integer.parseInt(result.getString(8))- item.get(i);
							String beliQuery = "UPDATE `progin_13511059`.`barang` SET `n_beli` = '"+newPembelian+"', `stok` = '"+newStok+"' WHERE `barang`.`id_barang` = '"+shopping_cart.get(i)+"'";
							state2 = con.createStatement();
							state2.executeUpdate(beliQuery);
							session.setAttribute("shopping_cart", null);
							session.setAttribute("shopping_request", null);
							session.setAttribute("mount", null);
							out.print("");
						}
					}
					}
				catch(SQLException e){
					System.out.println(e.getMessage());
					System.out.println("masuk sini");
				}
			}
		}
	}

}