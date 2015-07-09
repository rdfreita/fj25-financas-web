package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.caelum.financas.exception.ValorInvalidoException;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@Stateless
public class MovimentacaoDao {

	@PersistenceContext
	EntityManager manager;

	public void adiciona(Movimentacao movimentacao) {
		this.manager.persist(movimentacao);
		if(movimentacao.getValor().compareTo(BigDecimal.ZERO) < 0){
			throw new ValorInvalidoException("Movimentação negativa");
		}
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}
	
	@SuppressWarnings("unchecked")
	public List<Movimentacao> listaTodasmovimentacoes(Conta conta){
		return this.manager.createQuery("select m from Movimentacao m where m.conta = :conta order by m.valor desc")
		.setParameter("conta", conta)
		.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Movimentacao> listaPorValorETipo(BigDecimal valor, TipoMovimentacao tipo){
		return this.manager.createQuery("select m from Movimentacao m where m.valor = :valor and m.tipoMovimentacao = :tipo")
		.setParameter("valor", valor)
		.setParameter("tipo", tipo)
		.getResultList();
	}
}
