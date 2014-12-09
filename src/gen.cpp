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
#include <vector>

using namespace std;

#define REP(i, n) for(int i(0); (i)<(int)(n); i++)

int n = 3 + 1;
const int N = 40, M = 20;

char authors[N][M] = {
	"Saber", "Caster", "Rider", "Berserker",
	"Assassian", "Archer", "Lancer",
	"Lover", "Mocker", "Joker", "Batman",
	"Superman", "Fennman", "Mann", "Jun", "Feifei",
	"Idot", "Raper", "Thief"
};

char nuon[N][M] = {
	"Love", "Peace", "War", "Sentence", "Words", 
	"Dinner", "Relief", "Phone", "Tao", "Keys",
	"Mistery", "Secret", "Guide"
};

char adjs[N][M] = {
	"Great", "Weak", "Sad", "Historical", 
	"Numerous", "Fake", "Last", "First", 
	"Strange", "Interesting", "Disgusting"
};

string genName() {
	int a = -1;
	while (a == -1 || strlen(authors[a]) == 0) a = rand() % N;
	return string(authors[a]);
}
string genNuon() {
	int b = -1;
	while (b == -1 || strlen(nuon[b]) == 0) b = rand() % N;
	return string(nuon[b]);
}

string genTitle() {
	int c = -1;
	while (c == -1 || strlen(adjs[c]) == 0) c = rand() % N;
	return genName() + "'s " + string(adjs[c]) + " " + genNuon();
}

string genNumString(int len) {
	string s;
	REP(i, len) s += '0' + rand() % 10;
	return s;
}

int main() {
	srand(time(NULL));
	vector<pair<string, string> > user;
	REP(i, n) {
		cout << "register" << endl;
		string nn, un, ps;
		if (!i) {
			nn = "God";
			un = "root";
			ps = "123";
		} else {
			nn = genName();
			un = nn + genNumString(2);
			ps = genNumString(10);
		}
		user.push_back(make_pair(un, ps));
		cout << un << endl << ps << endl << nn << endl << "None" << endl << genNumString(11) << endl;
		cout << endl;
	}
	cout << endl;
	cout << "login" << endl << "root" << endl << "123" << endl;
	vector<string> bis;
	REP(i, n) if (i) {
		string iss = genNumString(13);
		bis.push_back(iss);
		cout << "newbook" << endl << iss << endl << genTitle() << endl;
		int cnt = rand() % 3 + 1;
		REP(j, cnt) {
			if (j) cout << ',';
			cout << genName();
		}
		cout << endl;
		cout << ((rand() & 1) ? "Devils" : "Devines") << endl;
		cout << genNumString(4) << endl;
		cout << rand()%100 + 10 << endl;
		cout << (rand()%1000 + 1) / 10.0 << endl;
		cout << "softcover" << endl;
		cout << genNuon() << endl;
	}
	
	int L = (int)bis.size();

	cout << endl;
	REP(i, n) if (i) {
		cout << "login" << endl << user[i].first << endl << user[i].second << endl;
		REP(j, n) if (j) {
			cout << "feedback" << endl << bis[j - 1] << endl << (rand()%11) << endl << endl;
		}
	}
	
	cout << endl;
	REP(i, n) if (i) {
		cout << "login" << endl << user[i].first << endl << user[i].second << endl;
		REP(j, n) if (j && j != i) {
			REP(k, n) if (k) {
				cout << "rate" << endl << user[j].first << endl << bis[k - 1] << endl << (rand() % 3) << endl;
			}
		}
	}
	cout << endl;

	REP(i, n) if (i) {
		cout << "login" << endl << user[i].first << endl << user[i].second << endl;
		REP(j, n) if (j) {
			cout << "order" << endl << bis[j - 1] << endl << (rand() % 2 + 1) << endl;
		}
	}
	cout << endl;
	cout << 0 << endl;
	return 0;
}

