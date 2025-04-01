/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Adm
 */

import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto(ProdutosDTO produto) {
        conn = new conectaDAO().connectDB(); // Estabelece a conexão

        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";

        try {
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());      // Define o nome
            prep.setDouble(2, produto.getValor());     // Define o preço
            prep.setString(3, "À venda");
            prep.executeUpdate(); // Executa a inserção

            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
        } finally {
            try {
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos() {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>(); // Cria uma nova lista para armazenar os produtos
        String sql = "SELECT * FROM produtos";  // SQL para buscar todos os produtos

        try {
            conn = new conectaDAO().connectDB();  // Estabelece a conexão com o banco de dados
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();   // Executa a consulta

            // Preenche a lista com os dados dos produtos
            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));          // Recupera o ID do produto
                produto.setNome(rs.getString("nome"));   // Recupera o nome
                produto.setValor(rs.getDouble("valor")); // Recupera o valor
                produto.setStatus(rs.getString("status"));// Recupera o status, se necessário
                listagem.add(produto); // Adiciona o produto à lista
            }

            rs.close();  // Fecha o ResultSet
            stmt.close();  // Fecha o PreparedStatement
            conn.close();  // Fecha a conexão

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
        }

        return listagem;  // Retorna a lista de produtos
    }
    
    public void venderProduto(int id) {
        String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?"; 

        try {
            conn = new conectaDAO().connectDB();  // Estabelece a conexão
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id); // Define o ID do produto que será atualizado

            int rowsAffected = stmt.executeUpdate(); // Executa a atualização

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Produto vendido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro: Produto não encontrado!");
            }

            stmt.close(); // Fecha o PreparedStatement
            conn.close(); // Fecha a conexão

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao vender produto: " + e.getMessage());
        }
    }
    
        public ArrayList<ProdutosDTO> listarProdutosVendidos() {
            ArrayList<ProdutosDTO> listagem = new ArrayList<>();
            String sql = "SELECT * FROM produtos WHERE status = 'Vendido'"; // Filtra apenas os vendidos

            try {
                conn = new conectaDAO().connectDB();  
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    ProdutosDTO produto = new ProdutosDTO();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setValor(rs.getDouble("valor"));
                    produto.setStatus(rs.getString("status"));

                    listagem.add(produto);
                }

                rs.close();
                stmt.close();
                conn.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao listar produtos vendidos: " + e.getMessage());
            }

            return listagem;  
        }
}

