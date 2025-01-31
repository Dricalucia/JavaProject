package br.gov.cesarschool.poo.bonusvendas.tela;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.time.LocalDate;
import javax.swing.JOptionPane;
import java.time.DateTimeException;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


import br.gov.cesarschool.poo.bonusvendas.dao.VendedorDAO;
import br.gov.cesarschool.poo.bonusvendas.entidade.Vendedor;
import br.gov.cesarschool.poo.bonusvendas.entidade.geral.Endereco;
import br.gov.cesarschool.poo.bonusvendas.entidade.geral.Sexo;
import br.gov.cesarschool.poo.bonusvendas.negocio.AcumuloResgateMediator;
import br.gov.cesarschool.poo.bonusvendas.negocio.ResultadoInclusaoVendedor;
import br.gov.cesarschool.poo.bonusvendas.negocio.VendedorMediator;
import org.eclipse.swt.widgets.Combo;

public class TelaManutencaoVendedorGUI {

	protected Shell shlTelaManutVendedor;
	
	static VendedorDAO vendedorDAO = new VendedorDAO();
	static AcumuloResgateMediator acumuloResgateMediator = new AcumuloResgateMediator();
	static VendedorMediator mediator = VendedorMediator.getInstance(vendedorDAO, acumuloResgateMediator);
	
	private Text txtNome;
	private Text txtRenda;
	private Text txtcpf;
	private Text txtLogradouro;
	private Text txtNumero;
	private Text txtComplemento;
	private Text txtCep;
	private Text txtCidade;
	private Text txtPais;
	private Combo txtEstado;
	private Button btnM;
	private Button btnF;
	private DateTime dateTime;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TelaManutencaoVendedorGUI window = new TelaManutencaoVendedorGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlTelaManutVendedor.open();
		shlTelaManutVendedor.layout();
		while (!shlTelaManutVendedor.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		
		shlTelaManutVendedor = new Shell();
		shlTelaManutVendedor.setSize(606, 340);
		shlTelaManutVendedor.setText("Tela de cadastro");
				
		// Definição do Botão Novo
		Button btnNovo = new Button(shlTelaManutVendedor, SWT.NONE);
		btnNovo.setBounds(10, 241, 90, 30);
		btnNovo.setText("Novo");
				
		btnNovo.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        String cpf = txtcpf.getText();
		        String nome = txtNome.getText();
		        String rendaStr = txtRenda.getText();
		        String logradouro = txtLogradouro.getText();
		        String numeroStr = txtNumero.getText();
		        String complemento = txtComplemento.getText();
		        String cep = txtCep.getText();
		        String cidade = txtCidade.getText();
		        String estado = txtEstado.getText();
		        String pais = txtPais.getText();
		        boolean sexoM = btnM.getSelection();
		        boolean sexoF = btnF.getSelection();
		        LocalDate dataNascimento = LocalDate.of(dateTime.getYear(), dateTime.getMonth() + 1, dateTime.getDay());

		        if (cpf.isEmpty() || nome.isEmpty() || rendaStr.isEmpty() || logradouro.isEmpty() ||
		            numeroStr.isEmpty() || complemento.isEmpty() || cep.isEmpty() || cidade.isEmpty() ||
		            estado.isEmpty() || pais.isEmpty() || (!sexoM && !sexoF)) {

		            JOptionPane.showMessageDialog(null, "Campos obrigatórios faltando.");
		            return;
		        }

		        try {
		        	/* Validação CPF */
		        	if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
		        	    JOptionPane.showMessageDialog(null, "CPF invalido. Deve conter 11 digitos.");
		        	    return;
		        	}
		        	
		        	/* Validação da Data de Nascimento */
		        	int day = dateTime.getDay();
		        	int month = dateTime.getMonth() + 1; 
		        	int year = dateTime.getYear();

		        	try {
		        	    LocalDate.of(year, month, day); 
		        	} catch (DateTimeException ex) {
		        	    JOptionPane.showMessageDialog(null, "Data de nascimento inválida. Use o formato dd/mm/yyyy.");
		        	    return;
		        	}

		        	/* Validação Verificação da Renda */
		        	try {
		        	    double renda = Double.parseDouble(rendaStr);
		        	} catch (NumberFormatException ex) {
		        	    JOptionPane.showMessageDialog(null, "Renda inválida. Use um número decimal válido com ponto como separador.");
		        	    return;
		        	}

		        	/* Validação Verificação do Número */
		        	if (numeroStr.length() > 7 || !numeroStr.matches("\\d{1,7}")) {
		        	    JOptionPane.showMessageDialog(null, "Número inválido. Deve ser um número inteiro com até 7 dígitos.");
		        	    return;
		        	}

		        	/* Validação do CEP */
		        	if (!cep.matches("\\d{2}\\.\\d{3}-\\d{2}|\\d{8}")) {
		        	    JOptionPane.showMessageDialog(null, "CEP inválido. Use o formato 99.999-99 ou 99999999.");
		        	    return;
		        	}
		        	
		            Sexo sexo = null;

		            if (btnM.getSelection()) {
		                sexo = Sexo.MASCULINO;
		            } else if (btnF.getSelection()) {
		                sexo = Sexo.FEMININO;
		            }

		            if (sexo == null) {
		                JOptionPane.showMessageDialog(null, "Selecione o sexo do vendedor.");
		                return;
		            }
		            
		            double renda = Double.parseDouble(rendaStr);
		            Endereco endereco = new Endereco(logradouro, Integer.parseInt(numeroStr), complemento, cep, cidade, estado, pais);

		            Vendedor novoVendedor = new Vendedor(cpf, nome, sexo, dataNascimento, renda, endereco);
		            ResultadoInclusaoVendedor resultado = mediator.incluir(novoVendedor);

		            if (resultado.getNumeroCaixaDeBonus() > 0) {
		                JOptionPane.showMessageDialog(null, "Vendedor incluído com sucesso. Número do caixa de bônus: " + resultado.getNumeroCaixaDeBonus());
		            } else {
		                JOptionPane.showMessageDialog(null, "Erro ao incluir o vendedor: " + resultado.getMensagemErroValidacao());
		            }
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(null, "Erro: A renda deve ser um número válido.");
		        }
		    }
		});
				
		// Definição do Botão de IncluirAlterar
		Button btnIncluirAlterar = new Button(shlTelaManutVendedor, SWT.NONE);
		btnIncluirAlterar.setEnabled(false);
		btnIncluirAlterar.setBounds(117, 241, 112, 30);
		btnIncluirAlterar.setText("Incluir/Alterar");
		
		// Definição do Botão Buscar
		Button btnBuscar = new Button(shlTelaManutVendedor, SWT.NONE);
		btnBuscar.setBounds(252, 241, 90, 30);
		btnBuscar.setText("Buscar");
				
		// Código do Botão de Buscar
		btnBuscar.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        String cpf = txtcpf.getText();

		        if (cpf.isEmpty()) {
		        	
		            JOptionPane.showMessageDialog(null, "Informe um CPF válido para buscar um vendedor.");
		            return;
		        }

		        Vendedor vendedor = mediator.buscar(cpf);

		        if (vendedor != null) {
		        	
		            // Preencha os campos do formulário com os dados do vendedor encontrado
		            txtNome.setText(vendedor.getNomeCompleto());
		            txtRenda.setText(Double.toString(vendedor.getRenda()));

		            // Resto do código para preencher os campos de endereço e outros dados...

		            JOptionPane.showMessageDialog(null, "Vendedor encontrado com sucesso.");
		        } 
		        
		        else {
		            JOptionPane.showMessageDialog(null, "Vendedor não encontrado.");
		        }
		    }
		});
				
		// Definição do Botão Limpar
		Button btnLimpar = new Button(shlTelaManutVendedor, SWT.NONE);
		btnLimpar.setBounds(367, 241, 90, 30);
		btnLimpar.setText("Limpar");
				
        // Código do Botão de Limpar
        btnLimpar.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                txtcpf.setText("");
                txtNome.setText("");
                txtRenda.setText("");
                btnM.setSelection(false);
                btnF.setSelection(false);
                dateTime.setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());
                txtLogradouro.setText("");
                txtNumero.setText("");
                txtComplemento.setText("");
                txtCep.setText("");
                txtCidade.setText("");
                txtEstado.setText("");
                txtPais.setText("");
            }
        });
				
		// Definição do Botão Cancelar
		Button btnCancelar = new Button(shlTelaManutVendedor, SWT.NONE);
		btnCancelar.setEnabled(false);
		btnCancelar.setBounds(488, 241, 90, 30);
		btnCancelar.setText("Cancelar");

		// Definição do Label CPF
		Label lblCpf = new Label(shlTelaManutVendedor, SWT.NONE);
		lblCpf.setText("CPF");
		lblCpf.setBounds(10, 24, 36, 20);
		
		txtcpf = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtcpf.setEnabled(true);
		txtcpf.setEnabled(true);
		txtcpf.setBounds(64, 21, 225, 26);
		
		// Definição do Label Nome
		Label lblNome = new Label(shlTelaManutVendedor, SWT.NONE);
		lblNome.setBounds(10, 61, 47, 20);
		lblNome.setText("Nome");
		
		txtNome = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtNome.setEnabled(true);
		txtNome.setEnabled(true);
		txtNome.setBounds(64, 58, 225, 26);
		
		// Definição do Label e Botão Check Sexo
		Label lblSexo = new Label(shlTelaManutVendedor, SWT.NONE);
		lblSexo.setEnabled(true);
		lblSexo.setText("Sexo");
		lblSexo.setBounds(10, 105, 47, 20);
		
				btnM = new Button(shlTelaManutVendedor, SWT.RADIO);
				btnM.setBounds(64, 105, 36, 20);
				btnM.setText("M");
				
				// CONDIÇÃO PARA QUE NÃO SEJA POSSÍVEL APERTAR AMBOS M E F
				btnM.addSelectionListener(new SelectionAdapter() {
				    @Override
				    public void widgetSelected(SelectionEvent e) {
				        if (btnM.getSelection()) {
				            // Botão "M" está selecionado
				            btnF.setSelection(false); // Desmarca o botão "F"
				        }
				    }
				});

		btnF = new Button(shlTelaManutVendedor, SWT.RADIO);
		btnF.setBounds(146, 105, 36, 20);
		btnF.setText("F");
		
		btnF.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent e) {
		        if (btnF.getSelection()) {
		            // Botão "F" está selecionado
		            btnM.setSelection(false); // Desmarca o botão "M"
		        }
		    }
		});
		
		// Definição do Label e Inserir Data
		Label lblDataDeNascimento = new Label(shlTelaManutVendedor, SWT.NONE);
		lblDataDeNascimento.setText("Data");
		lblDataDeNascimento.setBounds(10, 139, 36, 20);

		// DateTime dateTime = new DateTime(shlTelaManutVendedor, SWT.BORDER);
		dateTime = new DateTime(shlTelaManutVendedor, SWT.BORDER);
		dateTime.setBounds(64, 139, 118, 28);
		
		
		// Definição do Label Renda
		Label lblRenda = new Label(shlTelaManutVendedor, SWT.NONE);
		lblRenda.setBounds(10, 187, 47, 20);
		lblRenda.setText("Renda");
		
		txtRenda = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtRenda.setEnabled(true);
		txtRenda.setEnabled(true);
		txtRenda.setBounds(64, 184, 118, 26);
		
		// Definição do Label Endereço
		Label lblEndereo = new Label(shlTelaManutVendedor, SWT.NONE);
		lblEndereo.setText("Endereço");
		lblEndereo.setBounds(418, 10, 70, 20);

		// Definição do Label Logradouro
		txtLogradouro = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtLogradouro.setMessage("Logradouro");
		txtLogradouro.setToolTipText("");
		txtLogradouro.setEnabled(true);
		txtLogradouro.setEnabled(true);
		txtLogradouro.setBounds(339, 36, 239, 26);

		// Definição do Label Número
		txtNumero = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtNumero.setMessage("N°");
		txtNumero.setEnabled(true);
		txtNumero.setToolTipText("");
		txtNumero.setEnabled(true);
		txtNumero.setBounds(339, 68, 47, 26);
		
		// Definição do Label Complemento
		txtComplemento = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtComplemento.setMessage("Complemento");
		txtComplemento.setEnabled(true);
		txtComplemento.setToolTipText("");
		txtComplemento.setEnabled(true);
		txtComplemento.setBounds(392, 68, 186, 26);

		// Definição do Texto CEP
		txtCep = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtCep.setEnabled(true);
		txtCep.setToolTipText("");
		txtCep.setMessage("CEP");
		txtCep.setEnabled(true);
		txtCep.setBounds(339, 102, 239, 26);

		// Definição do Texto Cidade
		txtCidade = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtCidade.setEnabled(true);
		txtCidade.setToolTipText("");
		txtCidade.setMessage("Cidade");
		txtCidade.setEnabled(true);
		txtCidade.setBounds(339, 141, 148, 26);

		// Definição do Texto País
		txtPais = new Text(shlTelaManutVendedor, SWT.BORDER);
		txtPais.setEnabled(true);
		txtPais.setToolTipText("");
		txtPais.setMessage("País");
		txtPais.setEnabled(false);
		txtPais.setText("Brasil");
		txtPais.setBounds(339, 184, 239, 26);
		
		/* Check button criado porem não esta sendo usado */
		// Button btnCheckButton = new Button(shlTelaManutVendedor, SWT.CHECK);
		
		/* Inserir campo tipo combo para seleção do estado brasileiro */
		txtEstado = new Combo(shlTelaManutVendedor, SWT.DROP_DOWN |SWT.NONE);
		txtEstado.setBounds(495, 141, 82, 26);
		
		/* Lista dos estados brasileiros - ordem alfabética */
        String[] estados = {
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP","SE", "TO"
        };
        
        txtEstado.setItems(estados);
		
        txtEstado.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedState = txtEstado.getText();
			}
		});

		// Código do Botão de IncluirAlterar do Exemplo
/*	btnIncluirAlterar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Vendedor ent = new Vendedor(txtcpf.getText(), txtNome.getText(),
						Double.parseDouble(txtRenda.getText()));
				
				String msg = null;
				if (btnIncluirAlterar.getText().equals("Incluir")) {
					msg = mediator.incluir(ent);
				} else {
					msg = mediator.alterar(ent);
				}
				if (msg != null) {
					JOptionPane.showMessageDialog(null, 
					msg);					
				} else {
					btnIncluirAlterar.setEnabled(false);
					btnCancelar.setEnabled(false);
					txtNome.setEnabled(false);
					txtRenda.setEnabled(false);
					btnNovo.setEnabled(true);
					btnBuscar.setEnabled(true);
					txtCodigo.setEnabled(true);
					txtCodigo.setText("");
					txtRenda.setText("");
					txtNome.setText("");
					btnIncluirAlterar.setText("Alterar");
				}
			}
		}); */

	}
}