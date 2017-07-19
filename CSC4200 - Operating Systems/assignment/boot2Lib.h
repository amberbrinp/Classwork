struct buffer
{
	char buffer[128];
	int charCount;
	int head; 
	int tail;
};
typedef struct buffer buffer_t;

struct IDT_table
{
	unsigned short base_low16;
	unsigned short selector;
	unsigned char always0;
	unsigned char access;
	unsigned short base_high16;
};
typedef struct IDT_table idt_entry_t;


struct gdt_r_s
{
	unsigned short limit; 
	unsigned int base; 
} __attribute__((packed));
typedef struct gdt_r_s gdt_r_t;

struct PCB_s
{
	unsigned int* ESP;
	int PID;
	int row;
	int col;
}__attribute__((packed));
typedef struct PCB_s PCB;

struct queue
{
	PCB* q[10];
	int head;
	int tail;
	int count;
}__attribute__((packed));
typedef struct queue queue_t;

enum CSET_1 {
Q_PRESSED = 0x10, W_PRESSED = 0x11, E_PRESSED = 0x12, R_PRESSED = 0x13,
T_PRESSED = 0x14, Y_PRESSED = 0x15, U_PRESSED = 0x16, I_PRESSED = 0x17,
O_PRESSED = 0x18, P_PRESSED = 0x19
};
static char* cset_1_chars = "qwertyuiop";

enum CSET_2 {
A_PRESSED = 0x1E, S_PRESSED = 0x1F, D_PRESSED = 0x20, F_PRESSED = 0x21,
G_PRESSED = 0x22, H_PRESSED = 0x23, J_PRESSED = 0x24, K_PRESSED = 0x25,
L_PRESSED = 0x26
};
static char *cset_2_chars = "asdfghjkl";

enum CSET_3 {
Z_PRESSED = 0x2C, X_PRESSED = 0x2D, C_PRESSED = 0x2E, V_PRESSED = 0x2F,
B_PRESSED = 0x30, N_PRESSED = 0x31, M_PRESSED = 0x32,
};
static char *cset_3_chars = "zxcvbnm";

enum CSET_NUMBERS {
ONE_PRESSED = 0x2, TWO_PRESSED = 0x3, THREE_PRESSED = 0x4,
FOUR_PRESSED = 0x5, FIVE_PRESSED = 0x6, SIX_PRESSED = 0x7,
SEVEN_PRESSED = 0x8, EIGHT_PRESSED = 0x9, NINE_PRESSED = 0xA
};
static char *cset_4_nums = "123456789";

#define CSET_ZERO 0x0B
#define CSET_NL 0x1C
#define CSET_SPC 0x39
#define CSET_RET 0xE
#define CSET_POINT_PRESSED 0x34
#define CSET_SLASH_PRESSED 0x35

void show_eax();
void clearScr();
void writeScr(char *string, int row, int col);

void kbd_enter();
void lidtr(gdt_r_t idtr);
void outportb(unsigned short port, unsigned char byte);

void go();
void schedule();
void init_timer_dev(unsigned int ms);

unsigned int pushf_cli_fun();
void popf_fun(unsigned int eflags);

///////////////////////////////////////////////////////

void convert_num(unsigned int num, char buf[]);
int convert_num_h(unsigned int num, char buf[]);
void clearScr();

void kbd_handler(unsigned int scan);
char translate_scancode(int what);
char k_getchar();

void initIDT();
void initIDTEntry(idt_entry_t *entry, unsigned int base, unsigned short selector, unsigned char access);
void setupPIC();
void default_handler();

unsigned int* allocStack();
PCB* allocPCB();
void createProcess(unsigned int data_sel, unsigned int stack_sel, unsigned int stack_size, unsigned int code_sel, unsigned int process);
PCB* dequeue();
void enqueue(PCB* process);

void p1();
void clearscr_box(int r1, int c1, int r2, int c2);
int gets(char *s, int maxlen);
void p2();
int stoi(char *s);
int is_prime(int n);
int countPrimes(int max);

void vterm_foreground_next();
void vterm_block_if_background();
PCB* dequeue_VTq();
void enqueue_VTq(PCB* process);

