package br.com.mvp.maonamassa;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.mvp.controller.FuncionarioController;
import br.com.mvp.maonamassa.model.entity.Funcionario;
import br.com.mvp.maonamassa.model.repository.FuncionarioRepository;
import br.com.mvp.maonamassa.model.service.FuncionarioService;
import br.com.mvp.maonamassa.model.service.Util;

@SpringBootTest
class MaoNaMassaApplicationTests {

	@Autowired
	FuncionarioController ctrl;

	@Autowired
	FuncionarioService serv;

	@Autowired
	FuncionarioRepository repo;

	@Test
	void contextLoads() throws IOException {
		title("INÍCIO DAS TAREFAS");

		title("EXCLUI TODOS OS FUNCIONÁRIOS");
		excluiTodos();

		title("INSERINDO FUNCIONÁRIOS 3.1");
		atividade3_1();

		title("REMOVENDO O FUNCIONÁRIO \"JOÃO\" - 3.2");
		atividade3_2();

	}

	private static void title(String txt) {

		txt = "  " + txt;
		int tamanho = txt.length() + 4;
		if (tamanho < 80)
			tamanho = 80;
		System.out.println("=".repeat(tamanho));
		System.out.println("=".repeat(3) + txt);
		System.out.println("=".repeat(tamanho));
	}

	private void excluiTodos() {
		List<Funcionario> funcionarios = ctrl.getAllFuncionarios().getBody();
		if (!funcionarios.isEmpty()) {
			for (Funcionario funcionario : funcionarios) {
				ctrl.deleteFuncionario(funcionario.getIdPessoa());
			}
		}
	}

	// Insere a lista de funcionários
	private void atividade3_1() throws IOException {

		ctrl.saveFuncionario(
				serv.toDto("Maria", LocalDate.of(2000, Month.OCTOBER, 18), new BigDecimal(2009.44), "Operador"));
		ctrl.saveFuncionario(
				serv.toDto("João", LocalDate.of(1990, Month.MAY, 12), new BigDecimal(2284.38), "Operador"));
		ctrl.saveFuncionario(
				serv.toDto("Caio", LocalDate.of(1961, Month.MAY, 2), new BigDecimal(9836.14), "Coordenador"));
		ctrl.saveFuncionario(
				serv.toDto("Miguel", LocalDate.of(1988, Month.OCTOBER, 14), new BigDecimal(19119.88), "Diretor"));
		ctrl.saveFuncionario(
				serv.toDto("Alice", LocalDate.of(1995, Month.JANUARY, 5), new BigDecimal(2234.68), "Recepcionista"));
		ctrl.saveFuncionario(
				serv.toDto("Heitor", LocalDate.of(1999, Month.NOVEMBER, 19), new BigDecimal(1582.72), "Operador"));
		ctrl.saveFuncionario(
				serv.toDto("Arthur", LocalDate.of(1993, Month.MARCH, 31), new BigDecimal(4071.84), "Contador"));
		ctrl.saveFuncionario(
				serv.toDto("Laura", LocalDate.of(1994, Month.JULY, 8), new BigDecimal(3017.45), "Gerente"));
		ctrl.saveFuncionario(
				serv.toDto("Heloí­sa", LocalDate.of(2003, Month.MAY, 24), new BigDecimal(1606.85), "Eletricista"));
		ctrl.saveFuncionario(
				serv.toDto("Helena", LocalDate.of(1996, Month.SEPTEMBER, 2), new BigDecimal(2799.93), "Gerente"));

		Util.geraTxt(serv.listarTodosOsFuncionarios("TODOS OS FUNCIONÁRIOS", repo), "01-inserindo-funcionarios.txt");
	}

	private void atividade3_2() throws IOException {
		Funcionario joao = serv.getByNome("João");
		if (joao != null) {
			serv.deleteFuncionario(joao.getIdPessoa());
		}
		Util.geraTxt(serv.listarTodosOsFuncionarios("TODOS OS FUNCIONÁRIOS (SEM O \"JOÃO\")", repo),
				"02-excluindo-funcionario-joao.txt");
	}
}
