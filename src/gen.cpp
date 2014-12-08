#include <iostream>
#include <ctime>
#include <cctype>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cassert>
#include <algorithm>
#include <string>
#include <set>
#include <map>
#include <queue>
#include <stack>

using namespace std;

#define REP(i, n) for(int i(0); (i)<(int)(n); i++)

int main() {
	srand(time(NULL));
	REP(i, 11) {
		printf("register\n");
		if (!i) puts("root\n123\nTom\n1\n1");
		else printf("%d\n%d\n%d\n%d\n%d\n", i, i, i, 1, 1);
	}
	puts("");
	puts("login\nroot\n123");
	REP(i, 11) if (i) {
		printf("newbook\n%03d\n%05d\nDevil\n1984\n12\n12.5\nsoftcover\nSex\n", i, i);
	}
	puts("");
	REP(i, 11) if (i) {
		printf("login\n%d\n%d\n", i, i);
		REP(j, 11) if (j) {
			printf("feedback\n%03d\n%d\nRand\n", j, rand() % 10);
		}
	}
	puts("");
	REP(i, 11) if (i) {
		printf("login\n%d\n%d\n", i, i);
		REP(j, 11) if (j) {
			REP(k, 11) if (k) {
				printf("rate\n%d\n%03d\n%d\n", j, k, rand() % 3);
			}
		}
	}
	puts("");
	puts("0");
	return 0;
}

