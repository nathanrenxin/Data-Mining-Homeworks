clc;
E = csvread('example1.dat');
%E = csvread('example2.dat');
k = 4;
colours = {'red', 'green', 'blue', 'yellow', 'magenta', 'cyan', 'black', 'white'};

col1 = E(:,1);
col2 = E(:,2);
max_ids = max(max(col1,col2));
As= sparse(col1, col2, 1, max_ids, max_ids); 
A = full(As);

D = diag(sum(A,2));

L = (D^(-0.5))*A*(D^(-0.5));

[X,eigVs] = eigs(L,k);

% normalizing
Y = X./sqrt(sum(X.^2,2));

rng(1);

[idx,~] = kmeans(Y,k);

G = graph(A,'OmitSelfLoops');
p = plot(G,'layout','force','Marker','.','MarkerSize',10);
title('Graph');
axis equal

for i=1:k
    highlight(p,find(idx==i),'NodeColor',colours{i})
end

[v,M] = eigs(D-A,100, 'smallestreal');
figure,
plot(diag(M),'+');
title('Sorted eigenvalues');

fv = sort(v(:,2));
figure,
plot(fv,'+');
title('Sorted Fiedler Vector');

% plot 
figure,
hold on;
for i=1:size(A,1)
  c = idx(i,1);
  for j=1:size(A,2)  
    if A(i,j) == 1
        plot(i,j,'color', colours{c}, 'marker', '+');
    end  
  end  
end
hold off;
title('Clustered Data');