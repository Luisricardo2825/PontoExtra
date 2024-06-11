package beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import dao.DepartamentoDAO;
import dao.FuncionarioDAO;
import entidades.Funcionario;

@ManagedBean
public class FuncionarioBean {

    private String selectedOption;

	private Funcionario funcionarioSelecionado;

	private Date dataAtual;

	private Funcionario funcionario = new Funcionario();

	private List<Funcionario> funcionarios;

	/**
	 * Metodo usado para buscar um funcionario pelo ID de um Funcionario
	 * 
	 * @param funcionario
	 * @return Funcionario
	 */
	public Funcionario getById(int id) {
		for (Funcionario funcionario : funcionarios) {
			if (funcionario.getId() == id) {
				return funcionario;
			}
		}
		return null;
	}

	/**
	 * Metodo usado para buscar um funcionario pelo ID tendo um objeto
	 * "Funcionario" como parametro
	 * 
	 * @param funcionario
	 * @return Funcionario
	 */
	public Funcionario get(Funcionario funcionario) {
		for (Funcionario j : funcionarios) {
			if (j.getId() == funcionario.getId()) {
				return j;
			}
		}
		return null;
	}

	@PostConstruct
	public void initValues() {
		funcionario = new Funcionario();
		dataAtual = new Date();
		funcionarios = FuncionarioDAO.getAll();

	}

	/**
	 * Metodo usado para iniciar a edição de um item. Nota: Ele não possui nenhum
	 * código pois só é necessário para informar ao primefaces que é necessario
	 * atualizar a variavel "funcionarioSelecionado"
	 *
	 * @param funcionario
	 * @return Void
	 */
	public void initEdit(Funcionario funcionario) {
		if (funcionario != null)
			funcionarioSelecionado = funcionario;
	}

	/**
	 * Metodo usado para salvar um funcionario no banco de dados
	 *
	 * @param funcionario
	 * @return Void
	 */
	public void save() {

		try {
			funcionario.setDepartamento(DepartamentoDAO.getById(Integer.valueOf(selectedOption)));
			// Salva novo funcionario
			FuncionarioDAO.save(funcionario);
			// Adiciona o funcionario na lista de funcionarios
			funcionarios.add(funcionario);

			addMessage("Sucesso", "Salvo com sucesso! Id:" + funcionario.getId());

			// Instancia novo funcionario sem valores preenchidos(todos estão como "null")
			funcionario = new Funcionario();

		} catch (Exception erroAoSalvar) {
			erroAoSalvar.printStackTrace();
			String mensagemDeErro = erroAoSalvar.getLocalizedMessage();
			addMessage("Erro ao salvar", mensagemDeErro);
		}

	}

	/**
	 * Metodo usado para editar um funcionario cadastrado no banco de dados
	 *
	 * @param funcionario
	 * @return Void
	 */
	public void edit(Integer id) {
		if (id != null)
			funcionarioSelecionado = getById(id);

		try {

			// Atualiza o funcionario no banco de dados e adiciona a lista de funcionarios
			Funcionario novoDepartamento = FuncionarioDAO.update(funcionarioSelecionado);

			addMessage("Sucesso", "Editado com sucesso!" + novoDepartamento.getId());

			// Instancia novo funcionario sem valores preenchidos(todos estão como "null")
			funcionario = new Funcionario();

			funcionarios.add(novoDepartamento);

			funcionarios.remove(funcionarioSelecionado);
		} catch (Exception erroAoEditar) {
			erroAoEditar.printStackTrace();
			String mensagemDeErro = erroAoEditar.getLocalizedMessage();
			addMessage("Erro ao editar", mensagemDeErro);
		}

	}

	/**
	 * Metodo usado para deletar um funcionario cadastrado no banco de dados
	 *
	 * @param funcionario
	 * @return Void
	 */
	public void delete(Integer id) {
		if (id != null)
			funcionarioSelecionado = getById(id);

		// Deleta o funcionario do banco de dados
		FuncionarioDAO.delete(funcionarioSelecionado);

		// Remove o funcionario da lista de funcionarios
		funcionarios.remove(funcionarioSelecionado);
		funcionarioSelecionado = null;

		addMessage("Deletado", "Deletado com sucesso!");

	}

	// ---- GETTERS E SETTERS -----

	public void onDateTimeSelect(SelectEvent<LocalDateTime> event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		facesContext.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", event.getObject().format(formatter)));
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getFuncionarios() {
		// Ordena os funcionarios pelo ID
		funcionarios.sort((funcionario1, funcionario2) -> funcionario1.getId() - funcionario2.getId());

		return funcionarios;
	}

	public Funcionario getFuncionarioelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}

}
