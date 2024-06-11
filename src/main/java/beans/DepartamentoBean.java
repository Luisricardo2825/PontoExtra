package beans;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.event.SelectEvent;

import dao.DepartamentoDAO;
import entidades.Departamento;

@ManagedBean
public class DepartamentoBean {

	private Departamento departamentoSelecionado;

	private Date dataAtual;

	private Departamento departamento = new Departamento();

	private List<Departamento> departamentos;
	private List<SelectItem> departamentosSelect;

	/**
	 * Metodo usado para buscar um departamento pelo ID de um Departamento
	 * 
	 * @param departamento
	 * @return Departamento
	 */
	public Departamento getById(int id) {
		for (Departamento departamento : departamentos) {
			if (departamento.getId() == id) {
				return departamento;
			}
		}
		return null;
	}

	
	/**
	 * Metodo usado para buscar um departamento pelo ID tendo um objeto
	 * "Departamento" como parametro
	 * 
	 * @param departamento
	 * @return Departamento
	 */
	public Departamento get(Departamento departamento) {
		for (Departamento j : departamentos) {
			if (j.getId() == departamento.getId()) {
				return j;
			}
		}
		return null;
	}

	@PostConstruct
	public void initValues() {
		departamento = new Departamento();
		dataAtual = new Date();
		departamentos = DepartamentoDAO.getAll();
		setDepartamentosSelect(departamentos.stream().map(item->new SelectItem(item.getId(),item.getNome())).toList());

	}

	/**
	 * Metodo usado para iniciar a edição de um item. Nota: Ele não possui nenhum
	 * código pois só é necessário para informar ao primefaces que é necessario
	 * atualizar a variavel "departamentoSelecionado"
	 *
	 * @param departamento
	 * @return Void
	 */
	public void initEdit(Departamento departamento) {
		if (departamento != null)
			departamentoSelecionado = departamento;
	}

	/**
	 * Metodo usado para salvar um departamento no banco de dados
	 *
	 * @param departamento
	 * @return Void
	 */
	public void save() {

		try {
			// Salva novo departamento
			DepartamentoDAO.save(departamento);
			// Adiciona o departamento na lista de departamentos
			departamentos.add(departamento);

			addMessage("Sucesso", "Salvo com sucesso! Id:" + departamento.getId());

			// Instancia novo departamento sem valores preenchidos(todos estão como "null")
			departamento = new Departamento();

		} catch (Exception erroAoSalvar) {
			erroAoSalvar.printStackTrace();
			String mensagemDeErro = erroAoSalvar.getLocalizedMessage();
			addMessage("Erro ao salvar", mensagemDeErro);
		}

	}

	/**
	 * Metodo usado para editar um departamento cadastrado no banco de dados
	 *
	 * @param departamento
	 * @return Void
	 */
	public void edit(Integer id) {
		if (id != null)
			departamentoSelecionado = getById(id);

		try {

			// Atualiza o departamento no banco de dados e adiciona a lista de departamentos
			Departamento novoDepartamento = DepartamentoDAO.update(departamentoSelecionado);

			addMessage("Sucesso", "Editado com sucesso!" + novoDepartamento.getId());

			// Instancia novo departamento sem valores preenchidos(todos estão como "null")
			departamento = new Departamento();

			departamentos.add(novoDepartamento);

			departamentos.remove(departamentoSelecionado);
		} catch (Exception erroAoEditar) {
			erroAoEditar.printStackTrace();
			String mensagemDeErro = erroAoEditar.getLocalizedMessage();
			addMessage("Erro ao editar", mensagemDeErro);
		}

	}

	/**
	 * Metodo usado para deletar um departamento cadastrado no banco de dados
	 *
	 * @param departamento
	 * @return Void
	 */
	public void delete(Integer id) {
		if (id != null)
			departamentoSelecionado = getById(id);

		// Deleta o departamento do banco de dados
		DepartamentoDAO.delete(departamentoSelecionado);

		// Remove o departamento da lista de departamentos
		departamentos.remove(departamentoSelecionado);
		departamentoSelecionado = null;

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

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public List<Departamento> getDepartamentos() {
		// Ordena os departamentos pelo ID
		departamentos.sort((departamento1, departamento2) -> departamento1.getId() - departamento2.getId());

		return departamentos;
	}

	public Departamento getDepartamentoSelecionado() {
		return departamentoSelecionado;
	}

	public void setDepartamentoSelecionado(Departamento departamentoSelecionado) {
		this.departamentoSelecionado = departamentoSelecionado;
	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<SelectItem> getDepartamentosSelect() {
		return departamentosSelect;
	}

	public void setDepartamentosSelect(List<SelectItem> departamentosSelect) {
		this.departamentosSelect = departamentosSelect;
	}

}
