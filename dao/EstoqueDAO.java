package exemplo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exemplo.modelo.Estoque;

public class EstoqueDAO {
	
	public EstoqueDAO() {
		try {
			createTable();
		} catch (SQLException e) {
			//throw new RuntimeException("Erro ao criar tabela estoque");
			e.printStackTrace();
		}
	}
	
	// Cria a tabela se não existir
	private void createTable() throws SQLException {
		final String sqlCreate = "IF NOT EXISTS (" 
				+ "SELECT * FROM sys.tables t JOIN sys.schemas s ON "
				+ "(t.schema_id = s.schema_id) WHERE s.name = 'dbo'" 
				+ "AND t.name = 'Estoque')"
				+ "CREATE TABLE Estoque"
				+ " (id	int	IDENTITY,"
				+ "  nomeProduto	VARCHAR(255),"
				+ "  PRIMARY KEY (id))";
		
		Connection conn = DatabaseAccess.getConnection();
		
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCreate);
	}
	
	
	public List<Estoque> getAllEstoques() {
		Connection conn = DatabaseAccess.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		List<Estoque> estoques = new ArrayList<Estoque>();
		
		try {
			stmt = conn.createStatement();
			
			String SQL = "SELECT * FROM Estoque"; // consulta de SELECT
	        rs = stmt.executeQuery(SQL); // executa o SELECT
	        
	        while (rs.next()) {
	        	Estoque d = getEstoqueFromRs(rs);
	        	
	        	estoques.add(d);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getAllEstoque] Erro ao selecionar todo o estoque", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return estoques;		
	}
	
	public Estoque getEstoqueById(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		Estoque estoque = null;
		
		try {
			String SQL = "SELECT * FROM Estoque WHERE id = ?"; // consulta de SELECT
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        rs = stmt.executeQuery(); // executa o SELECT
	        
	        while (rs.next()) {
	        	estoque = getEstoqueFromRs(rs);
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[getEstoqueById] Erro ao selecionar o estoque por id", e);
		} finally {
			close(conn, stmt, rs);
		}
		
		return estoque;
	}
	
	public void insereEstoque(Estoque estoque) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "INSERT INTO Estoque (nome) VALUES (?)";
			stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
	    	stmt.setString(1, estoque.getNomeProduto()); // insira na segunda ? o nome da pessoa
	    	
			
	        stmt.executeUpdate(); // executa o SELECT
	        
	        rs = stmt.getGeneratedKeys();
	        
	        if (rs.next()) {
	        	estoque.setId(rs.getInt(1));
	        }
			
		} catch (SQLException e) {
			throw new RuntimeException("[insereEstoque] Erro ao inserir o estoque", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	public void deleteEstoque(int id) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
			
		try {
			String SQL = "DELETE Estoque WHERE id=?";
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			
	        stmt.executeUpdate(); 			
		} catch (SQLException e) {
			throw new RuntimeException("[deleteEstoque] Erro ao remover o estoque por id", e);
		} finally {
			close(conn, stmt, null);
		}
	}
	
	public void updateEstoque(Estoque estoque) {
		Connection conn = DatabaseAccess.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
				
		try {
			String SQL = "UPDATE Estoque SET nomeProduto = ? WHERE id=?";
			stmt = conn.prepareStatement(SQL);
	    	stmt.setString(1, estoque.getNomeProduto()); // insira na primeira ? o nome da pessoa
	    	// insira na última ? o id da pessoa
	    	stmt.setInt(2, estoque.getId());
	    	
	        stmt.executeUpdate(); // executa o UPDATE			
		} catch (SQLException e) {
			throw new RuntimeException("[updateEstoque] Erro ao atualizar o estoque", e);
		} finally {
			close(conn, stmt, rs);
		}
				
	}
	
	private Estoque getEstoqueFromRs(ResultSet rs) throws SQLException {
		Estoque d = new Estoque(); // cria um objeto de pessoa
		d.setId(rs.getInt("id")); // insere id recuperado do banco na pessoa
		d.setNomeProduto(rs.getString("nomeProduto")); // insere nome recuperado do banco na pessoa
		
		return d;
	}
	
	private void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) { rs.close(); }
			if (stmt != null) { stmt.close(); }
			if (conn != null) { conn.close(); }
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao fechar recursos.", e);
		}
	}
}
