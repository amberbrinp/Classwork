#include "boot2Lib.h"

buffer_t buff;
idt_entry_t idt[256];
queue_t RRq;
queue_t VTq;
PCB* currentPCB;
PCB* currentVTerm;

unsigned int stacks[10][1024];
PCB pcbs[10];
int next_pcb;
int next_stack;

int numProcesses;
//cset chars in boot2Lib


void default_handler()
{
	writeScr("Error", 0, 0);
}

void setupPIC() //outportb in boot2.S
{
 // set up cascading mode:
 	outportb(0x20, 0x11); // start 8259 master initialization
 	outportb(0xA0, 0x11); // start 8259 slave initialization
 	outportb(0x21, 0x20); // set master base interrupt vector (idt 32-38)
 	outportb(0xA1, 0x28); // set slave base interrupt vector (idt 39-45)
 // Tell the master that he has a slave:
 	outportb(0x21, 0x04); // set cascade ...
 	outportb(0xA1, 0x02); // on IRQ2
 // Enabled 8086 mode:
 	outportb(0x21, 0x01); // finish 8259 initialization
 	outportb(0xA1, 0x01);
 // Reset the IRQ masks
 	outportb(0x21, 0x0);
 	outportb(0xA1, 0x0);
 // Now, enable the keyboard & timer IRQ only 
 	outportb(0x21, 0xfc); // Turn on the keyboard & timer IRQ
 	outportb(0xA1, 0xff); // Turn off all others

}


void initIDTEntry(idt_entry_t *entry, unsigned int base, unsigned short selector, unsigned char access)
{
	entry->base_low16 = (unsigned short) base;
	entry->base_high16 = (unsigned short) (base>>16);
	entry->selector = selector;
	entry->access = access;
	entry->always0 = 0;
}

void initIDT()
{
	int i;
	for (i = 0; i < 256; i++)
	{
		if (i < 32)
			initIDTEntry(&idt[i], (unsigned int) default_handler, 16, 142);
		else if (i == 32)
			initIDTEntry(&idt[i], (unsigned int) schedule, 16, 142);
		else if (i == 33)
			initIDTEntry(&idt[i], (unsigned int) kbd_enter, 16, 142);
		else
			initIDTEntry(&idt[i], 0, 0, 0);
	}
	gdt_r_t idtr;
	idtr.limit = 2047;
	idtr.base = (unsigned int) idt;
	lidtr(idtr);//written in boot2.S
}

char k_getchar()
{
	char curr;
	if (buff.charCount == 0) return 0;
	curr = buff.buffer[buff.head];
	buff.head = (buff.head + 1) % 128;
	buff.charCount -= 1;
	return curr;
}

unsigned int* allocStack()
{
	return stacks[next_stack++];
}

PCB* allocPCB()
{
	return &pcbs[next_pcb++];
}

PCB* dequeue()
{
	PCB* block = RRq.q[RRq.head];
	RRq.head++;
	if (RRq.head > 9)
	{
		RRq.head = 0;
	}
	RRq.count--;
	return block;
}

void enqueue(PCB* process)
{
	RRq.q[RRq.tail] = process;
	RRq.tail++;
	if (RRq.tail > 9)
	{
		RRq.tail = 0;
	}
	RRq.count++;
}

void createProcess(unsigned int data_sel, unsigned int stack_sel, unsigned int stack_size, unsigned int code_sel, unsigned int process)
{
	//set up process's stack
	unsigned int* st = stack_size;
	st--;
	*st = 0x0200;
	st--;
	*st = code_sel;
	st--;
	*st = process;
	//st--;
	int i;
	for (i = 0; i < 8; i++)
	{
		st--;
		*st = 0; 
	}
	for (i = 0; i < 4; i++)
	{
		st--;
		*st = data_sel;
	}

	PCB* block = allocPCB();
	block->ESP = st;
	block->PID = next_pcb;
	block->row = (numProcesses * 10) + 1; //pre-increment
	block->col = 0;

	enqueue(block);
	numProcesses++;
	//enqueue the PCB* onto the round robin queue
}
void writeLine(char* buff)
{
	writeScr(buff,currentPCB->row,currentPCB->col);
	currentPCB->row++;
	
}
int main()
{

	clearScr();
	writeScr("Running ten processes.", 0, 0);

	initIDT();
	setupPIC();
	//program the timer
	init_timer_dev((unsigned int) 50);
	//initialize round robin queue that contains pointers to PCBs
	next_stack = 0;
	next_pcb = 0;

	RRq.head = 0;
	RRq.tail = 0;
	RRq.count = 0;

	VTq.head = 0;//?
	VTq.tail = 0; //?
	VTq.count = 0;

	currentPCB = 0;//?
	numProcesses = 0;
	//the PCB contains a value that represents the top of the stack (esp) and a process ID (both ints)
	unsigned int* s = allocStack();
	createProcess((unsigned int) 8, (unsigned int) 24, (unsigned int) s+1024, (unsigned int) 16, (unsigned int) p1);

	s = allocStack();
	createProcess((unsigned int) 8, (unsigned int) 24, (unsigned int) s+1024, (unsigned int) 16, (unsigned int) p2);
	
	
	currentVTerm = RRq.q[RRq.head];
	//enqueue_VTq(currentVTerm);
	go(); //pop first process off of queue, restore its state, and run the process
	while(1);
}

void p1()
{
  currentPCB->row = 1;
  char *msg = "Process p1: ";
  writeLine(msg);

  while (1)
  {
    currentPCB->row = 2;
    clearscr_box(2, 0, 9, 79);
    writeLine("Please enter a string less than 20 characters:");
    char s[20];
    char t[20];
    gets(s, 20);
    clearscr_box(3, 0, 4, 79);
    writeLine("You entered:");
    writeLine(s);
    writeLine("Hit enter to continue...");
    gets(t, 20);
    writeLine(s);
  }
  while(1);
}

int countPrimes(int max)
{
	if (max < 2) return 0;
	int numPrimes = 1;
	int i;
	for (i = 3; i < max; i += 2)
	{
		int isPrime = is_prime(i);
		if (isPrime == 1)
		{
			numPrimes++;
		}
	}
	return numPrimes;
}

int stoi(char *s)
{
	int i = 0;
	int count = 0;
	char buff[4];
	while (*s && count < 10)
	{
		if (*s >= '0' && *s <= '9')
		{
			i *= 10;
			i += (*s++ - '0');
			
		}
		else if(*s == ' ')
		{
			break;
		}
		else
		{
			i = 0;
			break;
		}
		count++;
	}
	return i;
}

int is_prime(int n)
{
  int i;
  if (n == 2) return 1;
  for (i = 2; i < n - 1; i++)
  {
    if (n % i == 0) return 0;
  }	
  return 1;
}

void p2()
{
  char *msg = "Process p2: ";
  writeScr(msg, 10, 0);

  while (1)
  {
    currentPCB->row = 11;
    clearscr_box(11, 0, 20, 79);
    writeLine("Please enter a number.");
    char s[20];
    char t[20];
    char buff[20];
    gets(s, 20);
    clearscr_box(12, 0, 13, 79);
    writeLine("The number of primes is");

    int num = stoi(s);
    int numPrimes = countPrimes(num);
    convert_num(numPrimes, buff);

    writeLine(buff);
    writeLine("Hit enter to continue...");
    gets(t, 20);
    writeLine(s);
  }
  while(1);
}

void clearscr_box(int r1, int c1, int r2, int c2)
{
	int i;
	int j;

	for (i = r1; i < r2; i++)
	{
		for (j = c1; j < c2; j++)
		{
			writeScr(" ", i, j);
		}
	}
}

int gets(char *s, int maxlen)
{
	char chars[2];
	int i;
	for (i = 0; i < maxlen-1; i++)
	{
		while(chars[0] == 0)
		{
			unsigned int eflags = pushf_cli_fun();
			vterm_block_if_background();
			chars[0] = k_getchar();
			popf_fun(eflags);
		}
		chars[1] = 0;
		if (chars[0] == '\n')
		{	
			while (i < maxlen-1)
			{
				s[i] = 0;//' ';
				i++;
			}
			return 0;
		}
		else
		{
			writeScr(chars, currentPCB->row, i);
			s[i] = chars[0];
		}
		chars[0] = 0;
	}
	return 1;
}

char translate_scancode(int what)
{
	if (what >= Q_PRESSED && what <= P_PRESSED)
		return cset_1_chars[what - Q_PRESSED];
	else if (what >= A_PRESSED && what <= L_PRESSED)
		return cset_2_chars[what - A_PRESSED];
	else if (what >= Z_PRESSED && what <= M_PRESSED)
		return cset_3_chars[what - Z_PRESSED];
	else if (what >= ONE_PRESSED && what <= NINE_PRESSED)
		return cset_4_nums[what - ONE_PRESSED];
	else if (what == CSET_ZERO)
		return '0';
	else if (what == CSET_NL || what == CSET_RET)
		return '\n';
	else if (what == CSET_SPC)
		return ' ';
	else if (what == CSET_POINT_PRESSED)
		return '.';
	else if (what == CSET_SLASH_PRESSED)
		return '/';
	else
		return 0;
}

void vterm_foreground_next()
{
	if (VTq.count > 0)
	{
		currentVTerm = dequeue_VTq();
		enqueue(currentVTerm);

		char buff[2];
		convert_num(currentVTerm->PID, buff);
		writeScr(buff, 0, 79);
	}
}

void vterm_block_if_background()
{
	while (currentPCB->PID != currentVTerm->PID)
	{
		if (VTq.count > 0)
		{
			PCB* process = dequeue_VTq();
			enqueue(process);
		}

		schedule_fun();
	}
}

void enqueue_VTq(PCB* process)
{
	VTq.q[VTq.tail] = process;
	VTq.tail++;
	if (VTq.tail > 9)
	{
		VTq.tail = 0;
	}
	VTq.count++;
}

PCB* dequeue_VTq()
{
	PCB* block = VTq.q[VTq.head];
	VTq.head++;
	if (VTq.head > 9)
	{
		VTq.head = 0;
	}
	VTq.count--;
	return block;
}

void kbd_handler(unsigned int scan)
{
	if (scan == 0 || buff.charCount == 128) return;
	if (scan == 0x3b)
	{
		vterm_foreground_next();
		return;
	}
	char curr = translate_scancode(scan);
	if (curr == 0) return;
	buff.buffer[buff.tail] = curr;
	buff.tail = (buff.tail + 1) % 128;
	buff.charCount += 1;
}



void clearScr()
{
	int i = 0;
	int j = 0;

	for (i = 0; i < 25; i++)
	{
		for (j = 0; j < 80; j++)
		{
			writeScr(" ", i, j);
		}
	}
}

int convert_num_h(unsigned int num, char buf[])
{
	if (num == 0) return 0;
	int idx = convert_num_h(num / 10, buf);
	buf[idx] = num % 10 + '0';
	buf[idx+1] = '\0';
	return idx + 1;
}


void convert_num(unsigned int num, char buf[])
{
  	if (num == 0) 
	{
    		buf[0] = '0';
   		buf[1] = '\0';
  	} 
	else
    		convert_num_h(num, buf);
}

