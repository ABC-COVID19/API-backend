import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRevision, Revision } from 'app/shared/model/revision.model';
import { RevisionService } from './revision.service';
import { RevisionComponent } from './revision.component';
import { RevisionDetailComponent } from './revision-detail.component';
import { RevisionUpdateComponent } from './revision-update.component';

@Injectable({ providedIn: 'root' })
export class RevisionResolve implements Resolve<IRevision> {
  constructor(private service: RevisionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRevision> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((revision: HttpResponse<Revision>) => {
          if (revision.body) {
            return of(revision.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Revision());
  }
}

export const revisionRoute: Routes = [
  {
    path: '',
    component: RevisionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Revisions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RevisionDetailComponent,
    resolve: {
      revision: RevisionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Revisions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RevisionUpdateComponent,
    resolve: {
      revision: RevisionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Revisions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RevisionUpdateComponent,
    resolve: {
      revision: RevisionResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Revisions'
    },
    canActivate: [UserRouteAccessService]
  }
];
